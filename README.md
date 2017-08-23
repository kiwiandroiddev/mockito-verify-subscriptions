# Mockito verification for RxJava subscriptions

This library extends Mockito to allow subscriptions on RxJava Observables returned from mocks to be verified.

Usage
-----

Verify that the observable returned from an invocation on a mock was subscribed to:

```kotlin
verify(mockClickProducer, wasSubscribed()).clicks()
```

Verify that it was never subscribed to:

```kotlin
verify(mockClickProducer, neverSubscribed()).clicks()
```

Argument matchers work just as with regular calls to verify:

```kotlin
verify(mockUserProvider, wasSubscribed()).getUser(withId = eq(00123))
```

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
