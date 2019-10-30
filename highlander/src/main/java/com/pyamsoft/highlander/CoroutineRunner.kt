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

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.yield
import java.util.concurrent.atomic.AtomicReference

/**
 * Adapted from https://gist.github.com/objcode/7ab4e7b1df8acd88696cb0ccecad16f7#file-concurrencyhelpers-kt-L91
 */
internal class CoroutineRunner<T> internal constructor(debug: Boolean) {

    private val logger = Logger(enabled = debug)
    private val activeTask = AtomicReference<Deferred<T>?>(null)

    suspend inline fun cancelAndRun(crossinline block: suspend () -> T): T {
        // Cancel if already running.
        cancelRunning()

        return coroutineScope {
            // Create a new coroutine, but don't start it until it's decided that this block should
            // execute. In the code below, calling await() on newTask will cause this coroutine to
            // start.
            val newTask = async(start = CoroutineStart.LAZY) { block() }
                newTask.invokeOnCompletion {
                    logger.log { "Runner task completed" }
                    if (activeTask.compareAndSet(newTask, null)) {
                        logger.log { "Completed runner task cleared" }
                    }
                }

            val result: T

            // Loop until we can become the active task
            while (true) {
                if (!activeTask.compareAndSet(null, newTask)) {
                    cancelRunning()

                    // Yield thread control to other waiting coroutines
                    logger.log { "Waiting for current task to cancel so we can begin" }
                    yield()
                } else {
                    // We are the active task, start running
                    logger.log { "Begin new task" }
                    result = newTask.await()
                    break
                }
            }

            logger.log { "Returning result from task" }
            return@coroutineScope result
        }
    }

    private suspend fun cancelRunning() {
        activeTask.get()
            ?.let { task ->
                logger.log { "Cancel running task" }
                task.cancelAndJoin()
            }
    }
}
