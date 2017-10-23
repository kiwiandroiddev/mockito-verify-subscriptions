package nz.co.kiwiandroiddev.mockito.rxjava.verification

import io.reactivex.Maybe
import nz.co.kiwiandroiddev.mockito.rxjava.verification.impl.SubscriptionOnDefaultMockedObservableEvent
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues
import org.mockito.internal.util.MockUtil
import org.mockito.invocation.InvocationOnMock
import rx.Completable
import rx.Observable
import rx.Single

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
        val methodReturnType = invocation.method.returnType
        return when {
            methodReturnType.isAssignableFrom(Observable::class.java) -> trackedObservable(invocation)
            methodReturnType.isAssignableFrom(Completable::class.java) -> trackedCompletable(invocation)
            methodReturnType.isAssignableFrom(Single::class.java) -> trackedSingle(invocation)
            methodReturnType.isAssignableFrom(io.reactivex.Observable::class.java) -> trackedRx2Observable(invocation)
            methodReturnType.isAssignableFrom(io.reactivex.Completable::class.java) -> trackedRx2Completable(invocation)
            methodReturnType.isAssignableFrom(io.reactivex.Single::class.java) -> trackedRx2Single(invocation)
            methodReturnType.isAssignableFrom(io.reactivex.Maybe::class.java) -> trackedRx2Maybe(invocation)
            methodReturnType.isAssignableFrom(io.reactivex.Flowable::class.java) -> trackedRx2Flowable(invocation)
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

    private fun trackedSingle(invocation: InvocationOnMock): Single<Any> =
            Single.defer {
                recordSubscriptionAsHiddenInvocationOnMock(realMethodInvocation = invocation)

                Single.error<Any>(RuntimeException("missing stub single for invocation: $invocation"))
            }

    private fun trackedRx2Observable(invocation: InvocationOnMock): io.reactivex.Observable<Any> =
            io.reactivex.Observable.defer {
                recordSubscriptionAsHiddenInvocationOnMock(realMethodInvocation = invocation)

                io.reactivex.Observable.error<Any>(RuntimeException("missing stub observable for invocation: $invocation"))
            }

    private fun trackedRx2Flowable(invocation: InvocationOnMock): io.reactivex.Flowable<Any> =
            io.reactivex.Flowable.defer {
                recordSubscriptionAsHiddenInvocationOnMock(realMethodInvocation = invocation)

                io.reactivex.Flowable.error<Any>(RuntimeException("missing stub observable for invocation: $invocation"))
            }

    private fun trackedRx2Completable(invocation: InvocationOnMock): io.reactivex.Completable =
            io.reactivex.Completable.defer {
                recordSubscriptionAsHiddenInvocationOnMock(realMethodInvocation = invocation)

                io.reactivex.Completable.error(RuntimeException("missing stub completable for invocation: $invocation"))
            }

    private fun trackedRx2Single(invocation: InvocationOnMock): io.reactivex.Single<Any> =
            io.reactivex.Single.defer {
                recordSubscriptionAsHiddenInvocationOnMock(realMethodInvocation = invocation)

                io.reactivex.Single.error<Any>(RuntimeException("missing stub single for invocation: $invocation"))
            }

    private fun trackedRx2Maybe(invocation: InvocationOnMock): Maybe<Any> =
            Maybe.defer {
                recordSubscriptionAsHiddenInvocationOnMock(realMethodInvocation = invocation)

                Maybe.error<Any>(RuntimeException("missing stub observable for invocation: $invocation"))
            }

    private fun recordSubscriptionAsHiddenInvocationOnMock(realMethodInvocation: InvocationOnMock) {
        val mockHandler = MockUtil.getMockHandler(realMethodInvocation.mock)

        val fakeMarkerInvocation = SubscriptionOnDefaultMockedObservableEvent(realMethodInvocation)

        // add a fake invocation so that the custom VerificationMode can detect it. The fake invocation will
        // also be cleared when the mock is reset!
        mockHandler.handle(fakeMarkerInvocation)
    }
}
