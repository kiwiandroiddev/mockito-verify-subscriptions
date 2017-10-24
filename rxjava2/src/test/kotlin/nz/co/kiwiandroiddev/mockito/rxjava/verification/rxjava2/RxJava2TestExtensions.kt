package nz.co.kiwiandroiddev.mockito.rxjava.verification.rxjava2

import io.reactivex.*
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber


/**
 * Created by matthewcl on 22/08/17.
 */
fun <T> Observable<T>.subscribeTestObserver(): TestObserver<T> {
    val testObserver = TestObserver.create<T>()
    this.subscribe(testObserver)
    return testObserver
}

fun <T> Flowable<T>.subscribeTestSubscriber(): TestSubscriber<T> {
    val testSubscriber = TestSubscriber.create<T>()
    this.subscribe(testSubscriber)
    return testSubscriber
}

fun <T> Single<T>.subscribeTestObserver(): TestObserver<T> {
    val testObserver = TestObserver.create<T>()
    this.subscribe(testObserver)
    return testObserver
}

fun Completable.subscribeTestObserver(): TestObserver<Unit> {
    val testSubscriber = TestObserver.create<Unit>()
    this.subscribe(testSubscriber)
    return testSubscriber
}

fun <T> Maybe<T>.subscribeTestObserver(): TestObserver<T> {
    val testSubscriber = TestObserver.create<T>()
    this.subscribe(testSubscriber)
    return testSubscriber
}