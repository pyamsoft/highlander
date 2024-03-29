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

android {
  namespace = "com.pyamsoft.highlander"

  kotlinOptions { freeCompilerArgs += "-Xexplicit-api=strict" }

  defaultConfig {
    // Android Testing
    // https://developer.android.com/training/testing/instrumented-tests
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
}

dependencies {
  coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")

  // AndroidX Annotations
  implementation("androidx.annotation:annotation:1.6.0")

  // Coroutines
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${rootProject.extra["coroutines"]}")

  // Testing
  testImplementation("org.jetbrains.kotlin:kotlin-test:${rootProject.extra["kotlin"]}")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${rootProject.extra["coroutines"]}")

  androidTestImplementation("androidx.test:runner:1.5.2")
  androidTestImplementation("org.jetbrains.kotlin:kotlin-test:${rootProject.extra["kotlin"]}")
  androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${rootProject.extra["coroutines"]}")
}
