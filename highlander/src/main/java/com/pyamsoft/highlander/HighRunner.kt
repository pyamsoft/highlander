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

internal class HighRunner<T> internal constructor(private val logger: Logger) {

    private val mutex = Mutex()
    private var activeRunner: Runner<T>? = null

    suspend fun run(block: suspend CoroutineScope.() -> T): T {
        // We must claim the mutex before checking task status because another task running in parallel
        // could be changing the activeTask value
        mutex.withLock {
            logger.log { "Checking for active task" }
            activeRunner?.also { active ->
                // Cancel if already running.
                val id = active.id
                val task = active.task
                logger.log { "Cancel active task: $id" }
                task.cancelAndJoin()
            }
        }

        // Make a new scope so that we will wait for all the work to be complete
        return coroutineScope {
            // Claim the lock and look for who's the active runner
            val runner = mutex.withLock {
                val active = activeRunner
                if (active != null) {
                    logger.log { "Found existing task, cancel: ${active.id}" }
                    active.task.cancelAndJoin()
                }

                val currentId = randomId()
                val newTask = async(start = CoroutineStart.LAZY) { block() }
                val newRunner = Runner(currentId, newTask)
                activeRunner = newRunner
                logger.log { "Marking task as active: $currentId" }
                return@withLock newRunner
            }

            // Await the completion of the task
            try {
                logger.log { "Awaiting task ${runner.id}" }
                val result = runner.task.await()
                logger.log { "Completed task ${runner.id}" }
                return@coroutineScope result
            } finally {
                // Make sure the activeTask is actually us, otherwise we don't need to do anything
                // Fast path in this case only since we have the id to guard with as well as the state
                // of activeTask
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
        }
    }

    internal data class Runner<T> internal constructor(
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
