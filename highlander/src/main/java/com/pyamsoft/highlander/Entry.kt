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

@file:JvmMultifileClass
@file:JvmName("Highlander")

package com.pyamsoft.highlander

import androidx.annotation.CheckResult
import com.pyamsoft.highlander.internal.BaseSwordsman
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope

/** Wrapper which will generate a Warrior object that delegates its call() to the upstream source */
@CheckResult
@JvmOverloads
public inline fun <R> highlander(
    context: CoroutineContext = HighlanderDefaults.DEFAULT_COROUTINE_CONTEXT,
    debugTag: String = "",
    crossinline upstream: suspend CoroutineScope.() -> R
): Warrior<R> {
  return object :
      BaseSwordsman<R>(
          context = context,
          debugTag = debugTag,
      ),
      Warrior<R> {

    override suspend fun call(): R {
      return warrior.call { upstream() }
    }
  }
}

/** Wrapper which will generate a Warrior object that delegates its call() to the upstream source */
@CheckResult
@JvmOverloads
public inline fun <R, T1> highlander(
    context: CoroutineContext = HighlanderDefaults.DEFAULT_COROUTINE_CONTEXT,
    debugTag: String = "",
    crossinline upstream: suspend CoroutineScope.(T1) -> R
): Warrior1<R, T1> {
  return object :
      BaseSwordsman<R>(
          context = context,
          debugTag = debugTag,
      ),
      Warrior1<R, T1> {

    override suspend fun call(p1: T1): R {
      return warrior.call { upstream(p1) }
    }
  }
}

/** Wrapper which will generate a Warrior object that delegates its call() to the upstream source */
@CheckResult
@JvmOverloads
public inline fun <R, T1, T2> highlander(
    context: CoroutineContext = HighlanderDefaults.DEFAULT_COROUTINE_CONTEXT,
    debugTag: String = "",
    crossinline upstream: suspend CoroutineScope.(T1, T2) -> R
): Warrior2<R, T1, T2> {
  return object :
      BaseSwordsman<R>(
          context = context,
          debugTag = debugTag,
      ),
      Warrior2<R, T1, T2> {

    override suspend fun call(
        p1: T1,
        p2: T2,
    ): R {
      return warrior.call { upstream(p1, p2) }
    }
  }
}

/** Wrapper which will generate a Warrior object that delegates its call() to the upstream source */
@CheckResult
@JvmOverloads
public inline fun <R, T1, T2, T3> highlander(
    context: CoroutineContext = HighlanderDefaults.DEFAULT_COROUTINE_CONTEXT,
    debugTag: String = "",
    crossinline upstream: suspend CoroutineScope.(T1, T2, T3) -> R
): Warrior3<R, T1, T2, T3> {
  return object :
      BaseSwordsman<R>(
          context = context,
          debugTag = debugTag,
      ),
      Warrior3<R, T1, T2, T3> {

    override suspend fun call(
        p1: T1,
        p2: T2,
        p3: T3,
    ): R {
      return warrior.call { upstream(p1, p2, p3) }
    }
  }
}

/** Wrapper which will generate a Warrior object that delegates its call() to the upstream source */
@CheckResult
@JvmOverloads
public inline fun <R, T1, T2, T3, T4> highlander(
    context: CoroutineContext = HighlanderDefaults.DEFAULT_COROUTINE_CONTEXT,
    debugTag: String = "",
    crossinline upstream: suspend CoroutineScope.(T1, T2, T3, T4) -> R
): Warrior4<R, T1, T2, T3, T4> {
  return object :
      BaseSwordsman<R>(
          context = context,
          debugTag = debugTag,
      ),
      Warrior4<R, T1, T2, T3, T4> {

    override suspend fun call(
        p1: T1,
        p2: T2,
        p3: T3,
        p4: T4,
    ): R {
      return warrior.call { upstream(p1, p2, p3, p4) }
    }
  }
}

/** Wrapper which will generate a Warrior object that delegates its call() to the upstream source */
@CheckResult
@JvmOverloads
public inline fun <R, T1, T2, T3, T4, T5> highlander(
    context: CoroutineContext = HighlanderDefaults.DEFAULT_COROUTINE_CONTEXT,
    debugTag: String = "",
    crossinline upstream: suspend CoroutineScope.(T1, T2, T3, T4, T5) -> R
): Warrior5<R, T1, T2, T3, T4, T5> {
  return object :
      BaseSwordsman<R>(
          context = context,
          debugTag = debugTag,
      ),
      Warrior5<R, T1, T2, T3, T4, T5> {

    override suspend fun call(
        p1: T1,
        p2: T2,
        p3: T3,
        p4: T4,
        p5: T5,
    ): R {
      return warrior.call { upstream(p1, p2, p3, p4, p5) }
    }
  }
}
