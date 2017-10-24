[![MIT License](http://img.shields.io/badge/license-MIT-green.svg) ](https://github.com/mockito/mockito/blob/master/LICENSE)
[![](https://jitpack.io/v/kiwiandroiddev/mockito-verify-subscriptions.svg)](https://jitpack.io/#kiwiandroiddev/mockito-verify-subscriptions)

# Mockito verification for RxJava subscriptions

[Mockito](http://site.mockito.org/) offers a powerful set of tools for JVM developers to create mock dependencies and verify particular method calls on them.

However, when your mocks return RxJava types (e.g. Observable, Single, Completable) you often want to go a step further and verify that the **reactive objects returned from your mocks were actually subscribed to**.

This is a small extension to Mockito to allow you to do this.

Basic Usage
-----------

First create your mock using `ReturnsTrackedObservables()` as its default Answer:
```kotlin
interface Repository {
    fun getItems(limit: Int = 1): Observable<String>
}

// ..
    
val mockRepository = Mockito.mock(Repository::class.java, ReturnsTrackedObservables())
```

Now you can verify subscriptions to Observables returned from it by passing `wasSubscribedTo()` as the second parameter to `verify`:

```kotlin
verify(mockRepository, wasSubscribedTo()).getItems()
```

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

Calling `reset` on a mock will reset recorded subscriptions, as with regular invocations:
```kotlin
mockRepository.getItems().subscribe({}, {})

reset(mockRepository)

verify(mockRepository, wasSubscribedTo()).getItems()    // will fail
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
