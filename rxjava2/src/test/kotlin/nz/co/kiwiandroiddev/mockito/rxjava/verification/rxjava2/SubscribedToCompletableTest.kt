package nz.co.kiwiandroiddev.mockito.rxjava.verification.rxjava2

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.isNull
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Completable
import nz.co.kiwiandroiddev.mockito.rxjava.verification.exceptions.TooLittleActualSubscriptions
import nz.co.kiwiandroiddev.mockito.rxjava.verification.exceptions.TooManyActualSubscriptions
import nz.co.kiwiandroiddev.mockito.rxjava.verification.exceptions.WantedButNotSubscribedTo
import nz.co.kiwiandroiddev.mockito.rxjava.verification.neverSubscribedTo
import nz.co.kiwiandroiddev.mockito.rxjava.verification.wasSubscribedTo
import org.junit.Before
import org.junit.Test
import org.mockito.exceptions.verification.WantedButNotInvoked

class SubscribedToCompletableTest {

    interface A {
        fun a(): Completable
        fun b(): Completable
        fun c(a: String?): Completable
    }

    lateinit var mock1: A
    lateinit var mock2: A

    @Before
    fun setUp() {
        mock1 = mock(defaultAnswer = ReturnsTrackedRx2Types())
        mock2 = mock(defaultAnswer = ReturnsTrackedRx2Types())
    }

    @Test(expected = WantedButNotInvoked::class)
    fun verifyOneVerification_methodNotCalled_shouldFail() {
        verify(mock1).a()
    }

    @Test(expected = WantedButNotSubscribedTo::class)
    fun verifySubscribedTo_methodNotCalled_shouldFail() {
        verify(mock1, wasSubscribedTo()).a()
    }

    @Test
    fun verifyOneInvocation_methodCalled_shouldPass() {
        mock1.a()

        verify(mock1).a()
    }

    @Test(expected = WantedButNotSubscribedTo::class)
    fun verifySubscribedTo_methodCalledAndNothingElse_shouldFail() {
        mock1.a()

        verify(mock1, wasSubscribedTo()).a()
    }

    @Test
    fun verifySubscribedTo_methodResultSubscribedTo_shouldPass() {
        mock1.a().subscribeTestObserver()

        verify(mock1, wasSubscribedTo()).a()
    }

    @Test(expected = TooManyActualSubscriptions::class)
    fun verifySubscribedTo_methodResultSubscribedToTwice_shouldFailWithTooManySubscriptions() {
        mock1.a().subscribeTestObserver()
        mock1.a().subscribeTestObserver()

        verify(mock1, wasSubscribedTo()).a()
    }

    @Test
    fun verifyOneInvocation_methodResultSubscribedTo_shouldPass() {
        mock1.a().subscribeTestObserver()

        verify(mock1, times(1)).a()
    }

    @Test(expected = WantedButNotSubscribedTo::class)
    fun verifySubscribedTo_mockResetAfterSubscription_shouldFail() {
        mock1.a().subscribeTestObserver()

        reset(mock1)

        verify(mock1, wasSubscribedTo()).a()
    }

    @Test(expected = WantedButNotSubscribedTo::class)
    fun verifySubscribedTo_differentMockSubscribedTo_shouldFail() {
        mock2.a().subscribeTestObserver()

        verify(mock1, wasSubscribedTo()).a()
    }

    @Test(expected = WantedButNotSubscribedTo::class)
    fun verifySubscribedTo_differentMethodSubscribedTo_shouldFail() {
        mock1.b().subscribeTestObserver()

        verify(mock1, wasSubscribedTo()).a()
    }

    @Test(expected = WantedButNotSubscribedTo::class)
    fun verifySubscribedTo_sameMethodDifferentArguments_shouldFail() {
        mock1.c("foo").subscribeTestObserver()

        verify(mock1, wasSubscribedTo()).c("bar")
    }

    @Test
    fun verifySubscribedTo_sameMethodSameArguments_shouldPass() {
        mock1.c("foo").subscribeTestObserver()

        verify(mock1, wasSubscribedTo()).c("foo")
    }

    @Test
    fun verifySubscribedTo_mutlipleCallsWithDifferentArguments_shouldPass() {
        mock1.c("foo").subscribeTestObserver()
        mock1.c("bar").subscribeTestObserver()

        verify(mock1, wasSubscribedTo()).c("foo")
        verify(mock1, wasSubscribedTo()).c("bar")
    }

    @Test
    fun verifySubscribedTo_anyArgumentMatcherUsedAndMethodWasSubscribedTo_shouldPass() {
        mock1.c("foo").subscribeTestObserver()

        verify(mock1, wasSubscribedTo()).c(any())
    }

    @Test(expected = WantedButNotSubscribedTo::class)
    fun verifySubscribedTo_anyArgumentMatcherUsedAndMethodWasNotSubscribedTo_shouldFail() {
        verify(mock1, wasSubscribedTo()).c(any())
    }

    @Test
    fun verifySubscribedTo_equalArgumentMatcherUsedAndMethodWasSubscribedTo_shouldPass() {
        mock1.c("foobar").subscribeTestObserver()

        verify(mock1, wasSubscribedTo()).c(eq("foobar"))
    }

    @Test(expected = WantedButNotSubscribedTo::class)
    fun verifySubscribedTo_isNullArgumentMatcherUsedAndMethodWasSubscribedToWithNonNullParam_shouldFail() {
        mock1.c("foobar").subscribeTestObserver()

        verify(mock1, wasSubscribedTo()).c(isNull<String>())
    }

    @Test
    fun verifySubscribedTo_isNullArgumentMatcherUsedAndMethodWasSubscribedToWithNullParam_shouldPass() {
        mock1.c(null).subscribeTestObserver()

        verify(mock1, wasSubscribedTo()).c(isNull<String>())
    }

    @Test
    fun verifyNeverSubscribedTo_notSubscribedTo_shouldPass() {
        verify(mock1, neverSubscribedTo()).a()
    }

    @Test(expected = TooManyActualSubscriptions::class)
    fun verifyNeverSubscribedTo_wasSubscribedTo_shouldFail() {
        mock1.a().subscribeTestObserver()

        verify(mock1, neverSubscribedTo()).a()
    }

    @Test(expected = WantedButNotSubscribedTo::class)
    fun verifySubscribedToTwice_notSubscribedTo_shouldFail() {
        verify(mock1, wasSubscribedTo(times = 2)).a()
    }

    @Test(expected = TooLittleActualSubscriptions::class)
    fun verifySubscribedToTwice_subscribedToOnce_shouldFail() {
        mock1.a().subscribeTestObserver()

        verify(mock1, wasSubscribedTo(times = 2)).a()
    }

    @Test
    fun defaultObservableSubscribedTo_shouldThrowErrorAboutMissingStub() {
        val error = mock1.a().subscribeTestObserver()
                .errors().single()

        assert(error.message == "missing stub completable for invocation: a.a();")
    }

}