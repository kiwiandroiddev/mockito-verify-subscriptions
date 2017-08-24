package nz.co.kiwiandroiddev.mockito.rxjava.verification

import nz.co.kiwiandroiddev.mockito.rxjava.verification.impl.SubscriptionOnDefaultMockedObservableEvent
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues
import org.mockito.internal.util.MockUtil
import org.mockito.invocation.InvocationOnMock
import rx.Completable
import rx.Observable

/**
 * Extends {@code org.mockito.internal.stubbing.defaultanswers.ReturnEmptyValues} to return a
 * default observable for observable-returning methods.
 *
 * This default observable enables two things:
 *
 * - The number of subscriptions to this default observable can be verified with
 * verify(mock, wasSubscribedTo())
 *
 * - The observable will invokes an {@link Observer}'s {@link Observer#onError onError} method
 * when the Observer subscribes to it, reminding you that the method hasn't been stubbed yet
 * (cf. a NPE would usually be thrown for unstubbed observable-returning methods when
 * simply using ReturnsEmptyValues)
 *
 * To use:
 *
 * For individual mocks, create the mock with Mockito.mock(Class::class.java, ReturnsTrackedObservables())
 *
 * To use it with all mocks by default (i.e. globally), create a subclass of
 * DefaultMockitoConfiguration in package org.mockito.configuration and override its
 * getDefaultAnswer() method to return an instance of this class.
 */
class ReturnsTrackedObservables : ReturnsEmptyValues() {
    override fun answer(invocation: InvocationOnMock): Any? {
        val returnType = invocation.method.returnType
        return when {
            returnType.isAssignableFrom(Observable::class.java) -> trackedObservable(invocation)
            returnType.isAssignableFrom(Completable::class.java) -> trackedCompletable(invocation)
            else -> super.answer(invocation)
        }
    }

    private fun trackedObservable(invocation: InvocationOnMock): Observable<Any> =
            Observable.defer {
                recordSubscriptionAsHiddenInvocationOnMock(realMethodInvocation = invocation)

                Observable.error<Any>(RuntimeException("missing stub observable for invocation: $invocation"))
            }

    private fun trackedCompletable(invocation: InvocationOnMock): Completable =
            Completable.defer {
                recordSubscriptionAsHiddenInvocationOnMock(realMethodInvocation = invocation)

                Completable.error(RuntimeException("missing stub completable for invocation: $invocation"))
            }

    private fun recordSubscriptionAsHiddenInvocationOnMock(realMethodInvocation: InvocationOnMock) {
        val mockHandler = MockUtil.getMockHandler(realMethodInvocation.mock)

        val fakeMarkerInvocation = SubscriptionOnDefaultMockedObservableEvent(realMethodInvocation)

        // add a fake invocation so that the custom VerificationMode can detect it. The fake invocation will
        // also be cleared when the mock is reset!
        mockHandler.handle(fakeMarkerInvocation)
    }
}
