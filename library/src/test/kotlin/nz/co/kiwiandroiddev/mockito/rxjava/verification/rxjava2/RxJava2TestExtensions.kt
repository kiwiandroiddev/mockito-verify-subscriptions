package nz.co.kiwiandroiddev.mockito.rxjava.verification.rxjava2

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver


/**
 * Created by matthewcl on 22/08/17.
 */
fun <T> Observable<T>.subscribeTestObserver(): TestObserver<T> {
    val testObserver = TestObserver.create<T>()
    this.subscribe(testObserver)
    return testObserver
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