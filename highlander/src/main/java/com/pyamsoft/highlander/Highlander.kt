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

