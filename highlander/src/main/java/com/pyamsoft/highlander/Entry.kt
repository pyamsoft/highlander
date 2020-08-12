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

import kotlinx.coroutines.CoroutineScope

/**
 * Wrapper which will generate a Warrior object that delegates its call() to the upstream source
 */
@JvmOverloads
fun <R> highlander(
    debugTag: String = "",
    upstream: suspend CoroutineScope.() -> R
): Warrior<R> {
    return object : Warrior<R> {

        private val warrior = ActualWarrior<R>(debugTag)

        override suspend fun call(): R {
            return warrior.call { upstream() }
        }
    }
}

/**
 * Wrapper which will generate a Warrior object that delegates its call() to the upstream source
 */
@JvmOverloads
fun <R, T1> highlander(
    debugTag: String = "",
    upstream: suspend CoroutineScope.(T1) -> R
): Warrior1<R, T1> {
    return object : Warrior1<R, T1> {

        private val warrior = ActualWarrior<R>(debugTag)

        override suspend fun call(p1: T1): R {
            return warrior.call { upstream(p1) }
        }
    }
}

/**
 * Wrapper which will generate a Warrior object that delegates its call() to the upstream source
 */
@JvmOverloads
fun <R, T1, T2> highlander(
    debugTag: String = "",
    upstream: suspend CoroutineScope.(T1, T2) -> R
): Warrior2<R, T1, T2> {
    return object : Warrior2<R, T1, T2> {

        private val warrior = ActualWarrior<R>(debugTag)

        override suspend fun call(
            p1: T1,
            p2: T2
        ): R {
            return warrior.call { upstream(p1, p2) }
        }
    }
}

/**
 * Wrapper which will generate a Warrior object that delegates its call() to the upstream source
 */
@JvmOverloads
fun <R, T1, T2, T3> highlander(
    debugTag: String = "",
    upstream: suspend CoroutineScope.(T1, T2, T3) -> R
): Warrior3<R, T1, T2, T3> {
    return object : Warrior3<R, T1, T2, T3> {

        private val warrior = ActualWarrior<R>(debugTag)

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
 * Wrapper which will generate a Warrior object that delegates its call() to the upstream source
 */
@JvmOverloads
fun <R, T1, T2, T3, T4> highlander(
    debugTag: String = "",
    upstream: suspend CoroutineScope.(T1, T2, T3, T4) -> R
): Warrior4<R, T1, T2, T3, T4> {
    return object : Warrior4<R, T1, T2, T3, T4> {

        private val warrior = ActualWarrior<R>(debugTag)

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
 * Wrapper which will generate a Warrior object that delegates its call() to the upstream source
 */
@JvmOverloads
fun <R, T1, T2, T3, T4, T5> highlander(
    debugTag: String = "",
    upstream: suspend CoroutineScope.(T1, T2, T3, T4, T5) -> R
): Warrior5<R, T1, T2, T3, T4, T5> {
    return object : Warrior5<R, T1, T2, T3, T4, T5> {

        private val warrior = ActualWarrior<R>(debugTag)

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
 * Wrapper which will generate a Warrior object that delegates its call() to the upstream source
 */
@JvmOverloads
fun <R, T1, T2, T3, T4, T5, T6> highlander(
    debugTag: String = "",
    upstream: suspend CoroutineScope.(T1, T2, T3, T4, T5, T6) -> R
): Warrior6<R, T1, T2, T3, T4, T5, T6> {
    return object : Warrior6<R, T1, T2, T3, T4, T5, T6> {

        private val warrior = ActualWarrior<R>(debugTag)

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
 * Wrapper which will generate a Warrior object that delegates its call() to the upstream source
 */
@JvmOverloads
fun <R, T1, T2, T3, T4, T5, T6, T7> highlander(
    debugTag: String = "",
    upstream: suspend CoroutineScope.(T1, T2, T3, T4, T5, T6, T7) -> R
): Warrior7<R, T1, T2, T3, T4, T5, T6, T7> {
    return object : Warrior7<R, T1, T2, T3, T4, T5, T6, T7> {

        private val warrior = ActualWarrior<R>(debugTag)

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
 * Wrapper which will generate a Warrior object that delegates its call() to the upstream source
 */
@JvmOverloads
fun <R, T1, T2, T3, T4, T5, T6, T7, T8> highlander(
    debugTag: String = "",
    upstream: suspend CoroutineScope.(T1, T2, T3, T4, T5, T6, T7, T8) -> R
): Warrior8<R, T1, T2, T3, T4, T5, T6, T7, T8> {
    return object : Warrior8<R, T1, T2, T3, T4, T5, T6, T7, T8> {

        private val warrior = ActualWarrior<R>(debugTag)

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
 * Wrapper which will generate a Warrior object that delegates its call() to the upstream source
 */
@JvmOverloads
fun <R, T1, T2, T3, T4, T5, T6, T7, T8, T9> highlander(
    debugTag: String = "",
    upstream: suspend CoroutineScope.(T1, T2, T3, T4, T5, T6, T7, T8, T9) -> R
): Warrior9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> {
    return object : Warrior9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> {

        private val warrior = ActualWarrior<R>(debugTag)

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
