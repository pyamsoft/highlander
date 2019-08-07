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

/**
 * Wrapper which will generate a Highlander object that delegates its call() to the upstream source
 */
@JvmOverloads
inline fun <reified R> highlander(
  debug: Boolean = false,
  crossinline upstream: suspend CoroutineScope.() -> R
): Highlander<R> {
  return object : Highlander<R> {

    private val warrior = ActualHighlander<R>(debug)

    override suspend fun call(): R {
      return warrior.call { upstream() }
    }

  }
}

/**
 * Wrapper which will generate a Highlander object that delegates its call() to the upstream source
 */
@JvmOverloads
inline fun <reified R, reified T1> highlander(
  debug: Boolean = false,
  crossinline upstream: suspend CoroutineScope.(T1) -> R
): Highlander1<R, T1> {
  return object : Highlander1<R, T1> {

    private val warrior = ActualHighlander<R>(debug)

    override suspend fun call(p1: T1): R {
      return warrior.call { upstream(p1) }
    }

  }
}

/**
 * Wrapper which will generate a Highlander object that delegates its call() to the upstream source
 */
@JvmOverloads
inline fun <reified R, reified T1, reified T2> highlander(
  debug: Boolean = false,
  crossinline upstream: suspend CoroutineScope.(T1, T2) -> R
): Highlander2<R, T1, T2> {
  return object : Highlander2<R, T1, T2> {

    private val warrior = ActualHighlander<R>(debug)

    override suspend fun call(
      p1: T1,
      p2: T2
    ): R {
      return warrior.call { upstream(p1, p2) }
    }

  }
}

/**
 * Wrapper which will generate a Highlander object that delegates its call() to the upstream source
 */
@JvmOverloads
inline fun <reified R, reified T1, reified T2, reified T3> highlander(
  debug: Boolean = false,
  crossinline upstream: suspend CoroutineScope.(T1, T2, T3) -> R
): Highlander3<R, T1, T2, T3> {
  return object : Highlander3<R, T1, T2, T3> {

    private val warrior = ActualHighlander<R>(debug)

    override suspend fun call(
      p1: T1,
      p2: T2,
      p3: T3
    ): R {
      return warrior.call { upstream(p1, p2, p3) }
    }

  }
}

/**
 * Wrapper which will generate a Highlander object that delegates its call() to the upstream source
 */
@JvmOverloads
inline fun <reified R, reified T1, reified T2, reified T3, reified T4> highlander(
  debug: Boolean = false,
  crossinline upstream: suspend CoroutineScope.(T1, T2, T3, T4) -> R
): Highlander4<R, T1, T2, T3, T4> {
  return object : Highlander4<R, T1, T2, T3, T4> {

    private val warrior = ActualHighlander<R>(debug)

    override suspend fun call(
      p1: T1,
      p2: T2,
      p3: T3,
      p4: T4
    ): R {
      return warrior.call { upstream(p1, p2, p3, p4) }
    }

  }
}

/**
 * Wrapper which will generate a Highlander object that delegates its call() to the upstream source
 */
@JvmOverloads
inline fun <reified R, reified T1, reified T2, reified T3, reified T4, reified T5> highlander(
  debug: Boolean = false,
  crossinline upstream: suspend CoroutineScope.(T1, T2, T3, T4, T5) -> R
): Highlander5<R, T1, T2, T3, T4, T5> {
  return object : Highlander5<R, T1, T2, T3, T4, T5> {

    private val warrior = ActualHighlander<R>(debug)

    override suspend fun call(
      p1: T1,
      p2: T2,
      p3: T3,
      p4: T4,
      p5: T5
    ): R {
      return warrior.call { upstream(p1, p2, p3, p4, p5) }
    }

  }
}

/**
 * Wrapper which will generate a Highlander object that delegates its call() to the upstream source
 */
@JvmOverloads
inline fun <reified R, reified T1, reified T2, reified T3, reified T4, reified T5, reified T6> highlander(
  debug: Boolean = false,
  crossinline upstream: suspend CoroutineScope.(T1, T2, T3, T4, T5, T6) -> R
): Highlander6<R, T1, T2, T3, T4, T5, T6> {
  return object : Highlander6<R, T1, T2, T3, T4, T5, T6> {

    private val warrior = ActualHighlander<R>(debug)

    override suspend fun call(
      p1: T1,
      p2: T2,
      p3: T3,
      p4: T4,
      p5: T5,
      p6: T6
    ): R {
      return warrior.call { upstream(p1, p2, p3, p4, p5, p6) }
    }

  }
}

/**
 * Wrapper which will generate a Highlander object that delegates its call() to the upstream source
 */
@JvmOverloads
inline fun <reified R, reified T1, reified T2, reified T3, reified T4, reified T5, reified T6, reified T7> highlander(
  debug: Boolean = false,
  crossinline upstream: suspend CoroutineScope.(T1, T2, T3, T4, T5, T6, T7) -> R
): Highlander7<R, T1, T2, T3, T4, T5, T6, T7> {
  return object : Highlander7<R, T1, T2, T3, T4, T5, T6, T7> {

    private val warrior = ActualHighlander<R>(debug)

    override suspend fun call(
      p1: T1,
      p2: T2,
      p3: T3,
      p4: T4,
      p5: T5,
      p6: T6,
      p7: T7
    ): R {
      return warrior.call { upstream(p1, p2, p3, p4, p5, p6, p7) }
    }

  }
}

/**
 * Wrapper which will generate a Highlander object that delegates its call() to the upstream source
 */
@JvmOverloads
inline fun <reified R, reified T1, reified T2, reified T3, reified T4, reified T5, reified T6, reified T7, reified T8> highlander(
  debug: Boolean = false,
  crossinline upstream: suspend CoroutineScope.(T1, T2, T3, T4, T5, T6, T7, T8) -> R
): Highlander8<R, T1, T2, T3, T4, T5, T6, T7, T8> {
  return object : Highlander8<R, T1, T2, T3, T4, T5, T6, T7, T8> {

    private val warrior = ActualHighlander<R>(debug)

    override suspend fun call(
      p1: T1,
      p2: T2,
      p3: T3,
      p4: T4,
      p5: T5,
      p6: T6,
      p7: T7,
      p8: T8
    ): R {
      return warrior.call { upstream(p1, p2, p3, p4, p5, p6, p7, p8) }
    }

  }
}

/**
 * Wrapper which will generate a Highlander object that delegates its call() to the upstream source
 */
@JvmOverloads
inline fun <reified R, reified T1, reified T2, reified T3, reified T4, reified T5, reified T6, reified T7, reified T8, reified T9> highlander(
  debug: Boolean = false,
  crossinline upstream: suspend CoroutineScope.(T1, T2, T3, T4, T5, T6, T7, T8, T9) -> R
): Highlander9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> {
  return object : Highlander9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> {

    private val warrior = ActualHighlander<R>(debug)

    override suspend fun call(
      p1: T1,
      p2: T2,
      p3: T3,
      p4: T4,
      p5: T5,
      p6: T6,
      p7: T7,
      p8: T8,
      p9: T9
    ): R {
      return warrior.call { upstream(p1, p2, p3, p4, p5, p6, p7, p8, p9) }
    }

  }
}

/**
 * Function wrapper which guarantees only a single running function call at a time using 0 parameters
 */
interface Highlander<R> {

  /**
   * Run a function and guarantee it is only running a single time
   */
  @CheckResult
  suspend fun call(): R

}

/**
 * Function wrapper which guarantees only a single running function call at a time using 1 parameter
 */
interface Highlander1<R, T1> {

  /**
   * Run a function and guarantee it is only running a single time
   */
  @CheckResult
  suspend fun call(p1: T1): R

}

/**
 * Function wrapper which guarantees only a single running function call at a time using 2 parameters
 */
interface Highlander2<R, T1, T2> {

  /**
   * Run a function and guarantee it is only running a single time
   */
  @CheckResult
  suspend fun call(
    p1: T1,
    p2: T2
  ): R

}

/**
 * Function wrapper which guarantees only a single running function call at a time using 3 parameters
 */
interface Highlander3<R, T1, T2, T3> {

  /**
   * Run a function and guarantee it is only running a single time
   */
  @CheckResult
  suspend fun call(
    p1: T1,
    p2: T2,
    p3: T3
  ): R

}

/**
 * Function wrapper which guarantees only a single running function call at a time using 4 parameters
 */
interface Highlander4<R, T1, T2, T3, T4> {

  /**
   * Run a function and guarantee it is only running a single time
   */
  @CheckResult
  suspend fun call(
    p1: T1,
    p2: T2,
    p3: T3,
    p4: T4
  ): R

}

/**
 * Function wrapper which guarantees only a single running function call at a time using 5 parameters
 */
interface Highlander5<R, T1, T2, T3, T4, T5> {

  /**
   * Run a function and guarantee it is only running a single time
   */
  @CheckResult
  suspend fun call(
    p1: T1,
    p2: T2,
    p3: T3,
    p4: T4,
    p5: T5
  ): R

}

/**
 * Function wrapper which guarantees only a single running function call at a time using 6 parameters
 */
interface Highlander6<R, T1, T2, T3, T4, T5, T6> {

  /**
   * Run a function and guarantee it is only running a single time
   */
  @CheckResult
  suspend fun call(
    p1: T1,
    p2: T2,
    p3: T3,
    p4: T4,
    p5: T5,
    p6: T6
  ): R

}

/**
 * Function wrapper which guarantees only a single running function call at a time using 7 parameters
 */
interface Highlander7<R, T1, T2, T3, T4, T5, T6, T7> {

  /**
   * Run a function and guarantee it is only running a single time
   */
  @CheckResult
  suspend fun call(
    p1: T1,
    p2: T2,
    p3: T3,
    p4: T4,
    p5: T5,
    p6: T6,
    p7: T7
  ): R

}

/**
 * Function wrapper which guarantees only a single running function call at a time using 8 parameters
 */
interface Highlander8<R, T1, T2, T3, T4, T5, T6, T7, T8> {

  /**
   * Run a function and guarantee it is only running a single time
   */
  @CheckResult
  suspend fun call(
    p1: T1,
    p2: T2,
    p3: T3,
    p4: T4,
    p5: T5,
    p6: T6,
    p7: T7,
    p8: T8
  ): R

}

/**
 * Function wrapper which guarantees only a single running function call at a time using 9 parameters
 */
interface Highlander9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> {

  /**
   * Run a function and guarantee it is only running a single time
   */
  @CheckResult
  suspend fun call(
    p1: T1,
    p2: T2,
    p3: T3,
    p4: T4,
    p5: T5,
    p6: T6,
    p7: T7,
    p8: T8,
    p9: T9
  ): R

}
