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

import com.pyamsoft.highlander.Swordsman
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/** Base class for a Swordsman object */
@PublishedApi
internal abstract class BaseSwordsman<R>
protected constructor(
    private val context: CoroutineContext,
    debugTag: String,
) : Swordsman {

  private val logger = Logger(debugTag)

  // Don't use protected to avoid exposing to public API
  // Don't use protected or else it's an IllegalAccessException at runtime
  internal val warrior: Connor<R> = Connor(context, logger)

  final override suspend fun cancel() =
      withContext(context = NonCancellable) {
          // Maybe we can simplify this with a withContext(context = NonCancellable +
          // context)
          // but I don't know enough about Coroutines right now to figure out if that works
          // or if plussing the contexts will remove NonCancel, so here we go instead.
          withContext(context = context) {
              // Coroutine scope here to make sure if anything throws an error we catch it in
              // the scope
              warrior.cancel()
          }
      }
}