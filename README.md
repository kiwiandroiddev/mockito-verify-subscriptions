[![MIT License](http://img.shields.io/badge/license-MIT-green.svg) ](https://github.com/mockito/mockito/blob/master/LICENSE)
[![](https://jitpack.io/v/kiwiandroiddev/mockito-verify-subscriptions.svg)](https://jitpack.io/#kiwiandroiddev/mockito-verify-subscriptions)

# Mockito verification for RxJava subscriptions

[Mockito](http://site.mockito.org/) offers a powerful set of tools for JVM developers to create mock dependencies and verify particular method calls on them.

However, when your mocks return RxJava types (e.g. Observable, Single, Completable) you often want to go a step further and verify that the **reactive objects returned from your mocks were actually subscribed to**.

This is a small extension to Mockito to allow you to do this.

Basic Usage
-----------

Given some dependency you'd like to mock:
```kotlin
interface Repository {
    fun getItems(): Observable<String>
}
```

Create your mock passing a `ReturnsTrackedRx1Observables()` (or `ReturnsTrackedRx2Observables()` for RxJava2) as its default [Answer](https://static.javadoc.io/org.mockito/mockito-core/2.10.0/org/mockito/stubbing/Answer.html):
```kotlin
val mockRepository = Mockito.mock(Repository::class.java, ReturnsTrackedRx1Observables())
```

Now you can verify subscriptions on it by passing `wasSubscribedTo()` to `verify` calls:

```kotlin
verify(mockRepository, wasSubscribedTo()).getItems()
```

This will cause your test to fail if the `Observable` returned by `getItems()` has not been subscribed to by the time it's run.

More Usage Examples
-------------------

Verify that the mock's return value was never subscribed to with:

```kotlin
verify(mockRepository, neverSubscribedTo()).getItems()
```

Argument matchers work as expected:

```kotlin
verify(mockRepository, wasSubscribedTo()).getItems(limit = eq(10))
```

Calling `reset` on a mock will reset recorded subscriptions, just like regular invocations:
```kotlin
mockRepository.getItems().subscribe({}, {})

reset(mockRepository)

verify(mockRepository, wasSubscribedTo()).getItems()    // will fail!
```

For more usage examples, check out the [Unit Tests](library/src/test/kotlin/nz/co/kiwiandroiddev/mockito/rxjava/verification/SubscribedToObservableTest.kt).

Limitations (TODO)
------------------

This is currently missing support for:
* In-order verifications
* No informative error when attempting to use the verification without creating the mock correctly
* Subscriptions can't be currently be tracked if you provide your own stubs for observable-returning methods

Download
--------

**Step 1.**

Add the JitPack repository to your root `build.gradle` at the end of repositories:

```groovy
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```
**Step 2.**

Add the dependency to your app's `build.gradle`

RxJava1:

```groovy
dependencies {
    compile 'com.github.kiwiandroiddev.mockito-verify-subscriptions:rxjava1:v0.2-alpha'
}
```

RxJava2:
```groovy
dependencies {
    compile 'com.github.kiwiandroiddev.mockito-verify-subscriptions:rxjava2:v0.2-alpha'
}
```
