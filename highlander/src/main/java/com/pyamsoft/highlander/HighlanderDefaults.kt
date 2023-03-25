/*
 * Copyright 2023 pyamsoft
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

@file:JvmMultifileClass
@file:JvmName("Highlander")

package com.pyamsoft.highlander

import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/** Global configuration object */
public object HighlanderDefaults {

  /** Is logging globally enabled */
  @JvmField public var LOGGING_ENABLED: Boolean = false

  /** The global default coroutine context for Highlander operations */
  @JvmField public var DEFAULT_COROUTINE_CONTEXT: CoroutineContext = EmptyCoroutineContext
}
