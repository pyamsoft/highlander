# Highlander

There can be only one.

A coroutine powered runner which guarantees that the only one instance of a
runner function is active at any given time.

## Install

In your `build.gradle`

```gradle
dependencies {
  def latestVersion = "0.0.18"

  implementation "com.github.pyamsoft:highlander:$latestVersion"
}
```

## Usage

Assuming something like:

```kotlin
fun onlyRunOneAtATime() {
  val result = async { api.call("/my/endpoint") }.await()
  doSomethingWithResult(result)
}

fun complexCancellingLogic() {
  ...
}

fun onButtonClicked () {
  complexCancellingLogic()
  onlyRunOneAtATime()
}
```

You can use Highlander to simplify your cancellation and run logic into:
```kotlin

private val highlander = highlander { onlyRunOneAtATime() }

fun onlyRunOneAtATime() {
  val result = async { api.call("/my/endpoint") }.await()
  doSomethingWithResult(result)
}

fun onButtonClicked () {
  highlander.call()
}
```

The highlander is coroutine safe and guarantees that your own runner logic
is completely cancelled before starting a new one. This avoids race conditions
and avoids the requirement of a mutex.

## Development

Highlander is developed in the Open on GitHub at:
```
https://github.com/pyamsoft/Highlander
```
If you know a few things about Android programming and are wanting to help
out with development you can do so by creating issue tickets to squash bugs,
and propose feature requests for future inclusion.`

# Issues or Questions

Please post any issues with the code in the Issues section on GitHub. Pull Requests
will be accepted on GitHub only after extensive reading and as long as the request
goes in line with the design of the application. Pull Requests will only be
accepted for new features of the application, for general purpose bug fixes, creating
an issue is simply faster.

## License

Apache 2

```
Copyright 2020 Peter Kenji Yamanaka

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

