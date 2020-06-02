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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID

internal class CoroutineRunner<T> internal constructor(debug: Boolean) {

    private val logger = Logger(enabled = debug)
    private val mutex = Mutex()
    private var activeTask: RunnerTask<T>? = null

    suspend inline fun run(crossinline block: suspend CoroutineScope.() -> T): T = coroutineScope {
        // Cancel if already running.
        mutex.withLock {
            activeTask?.also { active ->
                val id = active.id
                val task = active.task
                logger.log { "Cancel active task: $id" }
                task.cancelAndJoin()
            }
        }

        // Create a new coroutine, but don't start it until it's decided that this block should
        // execute. In the code below, calling await() on newTask will cause this coroutine to
        // start.
        val id = randomId()
        val newTask = async(start = CoroutineStart.LAZY) {
            logger.log { "Running task: $id" }
            return@async block()
        }
        newTask.invokeOnCompletion { logger.log { "Completed task: $id" } }

        // Make sure we mark this task as the active task
        mutex.withLock {
            val current = activeTask
            if (current == null || current.id != id) {
                logger.log { "Marking task as active: $id" }
                activeTask = RunnerTask(id, newTask)
            } else {
                val message = "New task is already active: $id"
                logger.error { message }
                throw IllegalStateException(message)
            }
        }

        try {
            // Await the completion of the task
            val result = newTask.await()
            logger.log { "Returning result from task[$id] $result" }
            return@coroutineScope result
        } finally {
            // Once the task finishes for any reason, we clear it as it is no longer active
            // It has either succeeded and returned, or encountered an error and thrown
            mutex.withLock {
                val current = activeTask
                if (current != null && current.id == id) {
                    logger.log { "Task finished, clearing: $id" }
                    activeTask = null
                } else {
                    val message = "Task finished but can't clear. Active: ${current?.id}, This: $id"
                    logger.error { message }
                    throw IllegalStateException(message)
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
