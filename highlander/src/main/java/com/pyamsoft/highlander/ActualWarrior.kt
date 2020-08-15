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

/**
 * Internal
 *
 * Runs an upstream function only after guaranteeing that all previous upstreams are cancelled.
 */
internal class ActualWarrior<R> internal constructor(debugTag: String) {

    private val logger = Logger(debugTag)
    private val runner = HighRunner<R>(logger)

    @CheckResult
    suspend fun call(upstream: suspend CoroutineScope.() -> R): R {
        logger.log { "Running call!" }
        return runner.run { upstream() }
    }
}