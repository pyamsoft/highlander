/*
 * Copyright 2022 Peter Kenji Yamanaka
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

package com.pyamsoft.highlander.internal

import androidx.annotation.CheckResult
import com.pyamsoft.highlander.Swordsman
import java.util.UUID
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart.LAZY
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/**
 * Connor is a warrior
 *
 * Runs an upstream function only after guaranteeing that all previous upstreams are cancelled.
 */
@PublishedApi
internal class Connor<R>
internal constructor(
    private val context: CoroutineContext,
    private val logger: Logger,
) : Swordsman {

  private val mutex: Mutex = Mutex()

  private var activeRunner: Runner<R>? = null

  /**
   * MUST CALL FROM INSIDE mutex.withLock {}
   *
   * Cancels the activeRunner if it is populated
   */
  private suspend fun cancelActiveTaskWhenLocked() {
    activeRunner?.also { runner ->
      logger.log { "Attempt to cancel existing: ${runner.id}" }

      logger.log { "Cancelling task: ${runner.id}" }
      runner.task.cancelAndJoin()
      logger.log { "Task cancel complete: ${runner.id}" }
    }
  }

  /**
   * We must claim the mutex before checking task status because another task running in parallel
   * could be changing the activeTask value
   */
  private suspend fun cancelExistingTask(): Unit =
      mutex.withLock {
        cancelActiveTaskWhenLocked()

        // Unset the active runner once we have cancelled any held task
        activeRunner = null
      }

  /** Claim the lock and look for who's the active runner */
  @CheckResult
  private suspend inline fun createNewTask(
      scope: CoroutineScope,
      crossinline block: suspend CoroutineScope.() -> R
  ): Runner<R> =
      mutex.withLock {
        // In case anything has taken the active runner in between
        cancelActiveTaskWhenLocked()

        return@withLock Runner(
                // Start the Deferred as Lazy so that it will not start
                // until we explicitly start() it
                task =
                    scope.async(
                        context = context,
                        start = LAZY,
                    ) {
                      block()
                    },
            )
            .also { runner ->
              activeRunner = runner
              logger.log { "Marking runner as active: ${runner.id}" }
            }
      }

  /** Await the completion of the task */
  @CheckResult
  private suspend fun runTask(runner: Runner<R>): R =
      withContext(context = context) {
        logger.log { "Running task ${runner.id}" }

        // Must call explicit start because this is a LAZY coroutine
        runner.task.start()

        logger.log { "Awaiting task ${runner.id}" }
        return@withContext runner.task.await().also { logger.log { "Completed task ${runner.id}" } }
      }

  /**
   * Make sure the activeTask is actually us, otherwise we don't need to do anything Fast path in
   * this case only since we have the id to guard with as well as the state of activeTask
   */
  private suspend fun releaseActiveTask(runner: Runner<R>) =
      withContext(context = NonCancellable) {
        // Run in the NonCancellable context because the mutex must be claimed to free the
        // activeTask or else we will leak memory.
        val initialRunner = activeRunner
        if (initialRunner?.id == runner.id) {
          logger.log { "ActiveRunner outside lock is current, clearing! ${runner.id}" }
          mutex.withLock {
            // Check again to make sure we really are the active task
            val lockedRunner = activeRunner
            if (lockedRunner?.id == runner.id) {
              logger.log { "Releasing ActiveRunner ${runner.id} since it is complete" }

              // Since this is called as finally, we don't need to cancel the task here.
              // It is either cancelled already or it is completed.
              // Just null out the field for memory.
              activeRunner = null
            }
          }
        }
      }

  /** The main entry point for the Warrior */
  suspend fun call(upstream: suspend CoroutineScope.() -> R): R =
      withContext(context = context) {
        logger.log { "Running call for warrior" }

        // Coroutine scope here to make sure if anything throws an error we catch it in the scope
        return@withContext coroutineScope {

          // Locks mutex
          cancelExistingTask()

          // Locks mutex
          val runner = createNewTask(this, upstream)
          return@coroutineScope try {
            runTask(runner)
          } finally {

            // Potentially locks mutex
            //
            // If something like this happens
            // call() --> creates task T1
            //    -- creates new task T1
            //    -- runs new task T1
            //    -- waits a long ass time for T1
            // call() --> creates task T2
            //    -- cancels T1
            //    -- depending on yield behavior:
            //    --   T1 may run finally block first and free itself
            //    --   T2 may enter create, which sets it as active
            //    -- upon T2 finally call, if it is the active task, it is freed
            //    -- otherwise T1 is freed when it claims the active task depending on yield
            releaseActiveTask(runner)
          }
        }
      }

  /** Cancel an in-progress function call outside of the normal warrior flow */
  override suspend fun cancel() =
      // NOTE(Peter): Can we can withContext(NonCancellable + context) for the same effect?
      //              I want this to not be cancellable and run on the thread context passed in
      withContext(context = NonCancellable) {
        // Run in the NonCancellable context because the mutex must be claimed to free the
        // activeTask or else we will leak memory.
        logger.log { "Explicitly cancelling warrior call" }

        // Run in the passed context
        withContext(context = context) {
          coroutineScope {
            logger.log { "Directly cancel existing task" }

            // Locks mutex
            cancelExistingTask()
          }
        }
      }

  /** Runner is a keyed Deferred task */
  private data class Runner<T>(
      val task: Deferred<T>,
      val id: String = randomId(),
  )

  companion object {

    /**
     * Generate a new random UUID
     *
     * @private
     */
    @JvmStatic
    @CheckResult
    private fun randomId(): String {
      return UUID.randomUUID().toString()
    }
  }
}
