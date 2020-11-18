/*
 * Copyright 2020 Peter Kenji Yamanaka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pyamsoft.highlander

import androidx.annotation.CheckResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.UUID

internal class HighRunner<T> internal constructor(private val logger: Logger) {

    private val mutex = Mutex()
    private var activeRunner: Runner<T>? = null

    /**
     * We must claim the mutex before checking task status because another task running in parallel
     * could be changing the activeTask value
     */
    private suspend fun cancelExistingTask() = mutex.withLock {
        logger.log { "Checking for active task" }
        activeRunner?.also { active ->
            // Cancel if already running.
            val id = active.id
            val task = active.task
            logger.log { "Cancel active task: $id" }
            task.cancelAndJoin()
        }
    }

    /**
     * Claim the lock and look for who's the active runner
     */
    private suspend inline fun createNewTask(
        scope: CoroutineScope,
        crossinline block: suspend CoroutineScope.() -> T
    ): Runner<T> = mutex.withLock {
        val active = activeRunner
        if (active != null) {
            logger.log { "Found existing task, cancel: ${active.id}" }
            active.task.cancelAndJoin()
        }

        val currentId = randomId()
        val newTask = scope.async(start = CoroutineStart.LAZY) { block() }
        val newRunner = Runner(currentId, newTask)
        activeRunner = newRunner
        logger.log { "Marking task as active: $currentId" }
        return@withLock newRunner
    }

    /**
     * Await the completion of the task
     */
    @CheckResult
    private suspend fun runTask(runner: Runner<T>): T {
        logger.log { "Awaiting task ${runner.id}" }
        val result = runner.task.await()
        logger.log { "Completed task ${runner.id}" }
        return result
    }

    /**
     * Make sure the activeTask is actually us, otherwise we don't need to do anything
     * Fast path in this case only since we have the id to guard with as well as the state
     * of activeTask
     */
    private suspend fun clearActiveTask(runner: Runner<T>) {
        if (activeRunner?.id == runner.id) {
            // Run in the NonCancellable context because the mutex must be claimed to free the activeTask
            // or else we will leak memory.
            withContext(context = NonCancellable) {
                mutex.withLock {
                    // Check again to make sure we really are the active task
                    if (activeRunner?.id == runner.id) {
                        logger.log { "Releasing activeTask ${runner.id} since it is complete" }
                        activeRunner = null
                    }
                }
            }
        }
    }

    suspend fun run(scope: CoroutineScope, block: suspend CoroutineScope.() -> T): T {
        cancelExistingTask()
        val runner = createNewTask(scope, block)
        return try {
            runTask(runner)
        } finally {
            clearActiveTask(runner)
        }
    }

    private data class Runner<T>(val id: String, val task: Deferred<T>)

    companion object {

        @JvmStatic
        @CheckResult
        private fun randomId(): String {
            return UUID.randomUUID().toString()
        }
    }
}
