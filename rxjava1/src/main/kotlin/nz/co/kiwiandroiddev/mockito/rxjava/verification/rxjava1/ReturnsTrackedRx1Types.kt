package nz.co.kiwiandroiddev.mockito.rxjava.verification.rxjava1

import nz.co.kiwiandroiddev.mockito.rxjava.verification.impl.SubscriptionOnDefaultMockedObservableEvent
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues
import org.mockito.internal.util.MockUtil.getMockHandler
import org.mockito.invocation.InvocationOnMock
import rx.Completable
import rx.Observable
import rx.Single

/**
 * Extends {@code org.mockito.internal.stubbing.defaultanswers.ReturnEmptyValues} to return default
 * RxJava1 reactive types (e.g. Observable, Single) from mock methods whose subscription events can be verified.
 *
 * More specifically, this enables two things:
 *
 * - The number of subscriptions on these default reactive objects can be verified with
 * verify(mock, wasSubscribedTo())
 *
 * - The reactive object will invoke its Observer's {@link Observer#onError onError} method
 * on subscription, reminding you that the method hasn't been stubbed yet
 * (cf. a NPE would usually be thrown for unstubbed observable-returning methods when
 * simply using ReturnsEmptyValues)
 *
 * To use:
 *
 * For individual mocks, create the mock with Mockito.mock(Class::class.java, ReturnsTrackedRx1Types())
 *
 * To use it with all mocks by default (i.e. globally), create a subclass of
 * DefaultMockitoConfiguration in package org.mockito.configuration and override its
 * getDefaultAnswer() method to return an instance of this class.
 */
class ReturnsTrackedRx1Types : ReturnsEmptyValues() {

    override fun answer(invocation: InvocationOnMock): Any? {
        val methodReturnType = invocation.method.returnType
        return when {
            methodReturnType.isAssignableFrom(Observable::class.java) -> trackedObservable(invocation)
            methodReturnType.isAssignableFrom(Completable::class.java) -> trackedCompletable(invocation)
            methodReturnType.isAssignableFrom(Single::class.java) -> trackedSingle(invocation)
            else -> super.answer(invocation)
        }
    }

    private fun trackedObservable(invocation: InvocationOnMock): Observable<Any> =
            Observable.defer {
                recordSubscriptionOnMock(realMethodInvocation = invocation)

                Observable.error<Any>(RuntimeException("missing stub observable for invocation: $invocation"))
            }

    private fun trackedCompletable(invocation: InvocationOnMock): Completable =
            Completable.defer {
                recordSubscriptionOnMock(realMethodInvocation = invocation)

                Completable.error(RuntimeException("missing stub completable for invocation: $invocation"))
            }

    private fun trackedSingle(invocation: InvocationOnMock): Single<Any> =
            Single.defer {
                recordSubscriptionOnMock(realMethodInvocation = invocation)

                Single.error<Any>(RuntimeException("missing stub single for invocation: $invocation"))
            }

    private fun recordSubscriptionOnMock(realMethodInvocation: InvocationOnMock) {
        val mockHandler = getMockHandler(realMethodInvocation.mock)

        val fakeMarkerInvocation = SubscriptionOnDefaultMockedObservableEvent(realMethodInvocation)

        // add a fake invocation so that the custom VerificationMode can detect it. The fake invocation will
        // also be cleared when the mock is reset!
        mockHandler.handle(fakeMarkerInvocation)
    }
}