/*
 * Copyright 2019 Peter Kenji Yamanaka
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
 *
 */

package com.pyamsoft.highlander

import androidx.annotation.CheckResult
import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

internal class CoroutineRunner<T> internal constructor(debug: Boolean) {

    private val logger = Logger(enabled = debug)
    private val mutex = Mutex()
    private var activeTask: RunnerTask<T>? = null

    suspend fun run(block: suspend CoroutineScope.() -> T): T = coroutineScope {
        // We must claim the mutex before checking task status because another task running in parallel
        // could be changing the activeTask value
        val currentId = randomId()
        val newTask: Deferred<T> = mutex.withLock {
            activeTask?.also { active ->
                // Cancel if already running.
                val id = active.id
                val task = active.task
                logger.log { "Cancel active task: $id" }
                task.cancelAndJoin()
            }

            // Create a new coroutine, but don't start it until it's decided that this block should
            // execute. In the code below, calling await() on newTask will cause this coroutine to
            // start.
            val lazyTask = async(start = CoroutineStart.LAZY) {
                logger.log { "Running task: $currentId" }
                return@async block()
            }

            // Make sure we mark this task as the active task
            logger.log { "Marking task as active: $currentId" }
            activeTask = RunnerTask(currentId, lazyTask)

            return@withLock lazyTask
        }

        // Await the completion of the task
        try {
            val result = newTask.await()
            logger.log { "Returning result from task $currentId" }
            return@coroutineScope result
        } finally {
            // Make sure the activeTask is actually us, otherwise we don't need to do anything
            // Fast path in this case only since we have the id to guard with as well as the state
            // of activeTask
            if (activeTask?.id == currentId) {
                // Run in the NonCancellable context because the mutex must be claimed to free the activeTask
                // or else we will leak memory.
                withContext(context = NonCancellable) {
                    mutex.withLock {
                        // Check again to make sure we really are the active task
                        if (activeTask?.id == currentId) {
                            logger.log { "Releasing activeTask $currentId since it is complete" }
                            activeTask = null
                        }
                    }
                }
            }
        }
    }

    internal data class RunnerTask<T> internal constructor(
        val id: String,
        val task: Deferred<T>
    )

    companion object {

        @CheckResult
        private fun randomId(): String {
            return UUID.randomUUID().toString()
        }
    }
}
