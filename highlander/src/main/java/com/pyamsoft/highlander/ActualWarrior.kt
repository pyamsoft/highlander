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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.UUID

/**
 * Internal
 *
 * Runs an upstream function only after guaranteeing that all previous upstreams are cancelled.
 */
@PublishedApi
internal class ActualWarrior<R> @PublishedApi internal constructor(debugTag: String) {

    /**
     * Logger
     *
     * @private
     */
    @PublishedApi
    internal val logger: Logger = Logger(debugTag)

    /**
     * Mutex
     *
     * @private
     */
    @PublishedApi
    internal val mutex: Mutex = Mutex()

    /**
     * The current runner
     *
     * @private
     */
    @PublishedApi
    internal var activeRunner: Runner<R>? = null

    /**
     * Cancel a task and log the id
     *
     * @private
     */
    @PublishedApi
    internal suspend fun cancelTask(id: String, task: Deferred<R>) {
        task.cancelAndJoin()
        logger.log { "Previously active task cancelled: $id" }
    }

    /**
     * We must claim the mutex before checking task status because another task running in parallel
     * could be changing the activeTask value
     *
     * @private
     */
    @PublishedApi
    internal suspend fun cancelExistingTask(): Unit = mutex.withLock {
        logger.log { "Checking for active task" }
        activeRunner?.also { active ->
            // Cancel if already running.
            val id = active.id
            val task = active.task
            cancelTask(id, task)
        }

        // Unset the active runner once we have cancelled any held task
        activeRunner = null
    }

    /**
     * Claim the lock and look for who's the active runner
     *
     * @private
     */
    @CheckResult
    @PublishedApi
    internal suspend inline fun createNewTask(
        scope: CoroutineScope,
        crossinline block: suspend CoroutineScope.() -> R
    ): Runner<R> = mutex.withLock {
        // In case anything has taken the active runner in between
        val active = activeRunner
        if (active != null) {
            cancelTask(active.id, active.task)
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
     *
     * @private
     */
    @CheckResult
    @PublishedApi
    internal suspend fun runTask(runner: Runner<R>): R {
        logger.log { "Awaiting task ${runner.id}" }
        runner.task.start()
        val result = runner.task.await()
        logger.log { "Completed task ${runner.id}" }
        return result
    }

    /**
     * Make sure the activeTask is actually us, otherwise we don't need to do anything
     * Fast path in this case only since we have the id to guard with as well as the state
     * of activeTask
     *
     * @private
     */
    @PublishedApi
    internal suspend fun clearActiveTask(runner: Runner<R>) {
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

    /**
     * The main entry point for the Warrior
     */
    @CheckResult
    suspend inline fun call(crossinline upstream: suspend CoroutineScope.() -> R): R =
        coroutineScope {
            cancelExistingTask()
            val runner = createNewTask(this, upstream)
            return@coroutineScope try {
                runTask(runner)
            } finally {
                clearActiveTask(runner)
            }
        }

    /**
     * Runner is a keyed Deferred task
     *
     * @private
     */
    @PublishedApi
    internal data class Runner<T> @PublishedApi internal constructor(
        val id: String,
        val task: Deferred<T>
    )

    companion object {

        /**
         * Generate a new random UUID
         *
         * @private
         */
        @JvmStatic
        @CheckResult
        @PublishedApi
        internal fun randomId(): String {
            return UUID.randomUUID().toString()
        }
    }
}
