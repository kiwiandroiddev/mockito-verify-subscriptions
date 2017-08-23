# Mockito verification for RxJava subscriptions

The [Mockito](http://site.mockito.org/) framework offers a powerful set of tools for verifying method calls on mocks. However, when testing interactions with mocks whose methods return RxJava Observables, verifying method calls is generally not enough. Rather, you want to know that  **Observables returned from the methods were actually subscribed to**.

This is a small extension to Mockito to allow subscriptions on Observables returned from mocks to be verified.

E.g. Given some repository interface with a method `getItems()` returning an `Observable`, whether that observable was subscribed to during a test can be verified with:

```kotlin
verify(mockRepository, wasSubscribedTo()).getItems()
```

Basic Usage
-----------

First create your mock using `ReturnsTrackedObservables()` as its default Answer:
```kotlin
val mockRepository = Mockito.mock(Repository::class.java, ReturnsTrackedObservables())
```

Now you can verify subscriptions to Observables returned from it by passing `wasSubscribedTo()` as the second parameter to `verify`:

```kotlin
verify(mockRepository, wasSubscribedTo()).getItems()
```

More Usage
----------

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

For more usage examples, check out the [Unit Tests](library/src/test/kotlin/nz/co/kiwiandroiddev/mockito/rxjava/verification/SubscribedToTest.kt).

Limitations
-----------

This is currently missing support for:
* `Single` and `Completable` RxJava 1 types
* RxJava 2 reactive types
* In-order verifications

Download
--------

**Step 1.**

Add the JitPack repository to your root `build.gradle` at the end of repositories:

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
**Step 2.**

Add the dependency to your app's `build.gradle`

```groovy
dependencies {
    compile 'com.github.kiwiandroiddev.mockito-verify-subscriptions:library:v0.1-alpha'
}
```
