package nz.co.kiwiandroiddev.mockito.rxjava.verification

import rx.Observable
import rx.observers.TestSubscriber

/**
 * Created by matthewcl on 22/08/17.
 */
fun <T> Observable<T>.subscribeTestSubscriber() : TestSubscriber<T> {
    val testSubscriber = TestSubscriber.create<T>()
    this.subscribe(testSubscriber)
    return testSubscriber
}