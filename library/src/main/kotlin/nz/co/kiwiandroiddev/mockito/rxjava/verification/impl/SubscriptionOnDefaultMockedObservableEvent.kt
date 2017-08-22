package nz.co.kiwiandroiddev.mockito.rxjava.verification.impl

import org.mockito.internal.debugging.LocationImpl
import org.mockito.invocation.Invocation
import org.mockito.invocation.InvocationOnMock
import org.mockito.invocation.Location
import org.mockito.invocation.StubInfo
import java.lang.reflect.Method

class SubscriptionOnDefaultMockedObservableEvent(
        val wrappedMethodInvocation: InvocationOnMock,
        private val callLocation: Location = LocationImpl()
) : Invocation {

    private val DONT_MATCH_ANY_INVOCATIONS = object {}

    override fun getArguments(): Array<Any> {
        return wrappedMethodInvocation.arguments
    }

    override fun getLocation(): Location = callLocation

    override fun isIgnoredForVerification(): Boolean = true

    override fun getMethod(): Method = wrappedMethodInvocation.method

    override fun ignoreForVerification() {
    }

    override fun getSequenceNumber(): Int {
        throw NotImplementedError()
    }

    override fun getRawReturnType(): Class<*> {
        throw NotImplementedError()
    }

    override fun isVerified(): Boolean = true

    override fun getMock(): Any {
        // This is to ensure that InvocationMatcher.matches(this) will always return false when passed any
        // FakeInvocationForRecordingSubscription objects. This is important because we don't want
        // these invocations to count towards the number of real invocations
        // (e.g. with verify(mock, times(2)).someObservable() )
        return DONT_MATCH_ANY_INVOCATIONS
    }

    override fun markVerified() {
    }

    override fun markStubbed(stubInfo: StubInfo?) {
    }

    override fun callRealMethod(): Any {
        throw NotImplementedError()
    }

    override fun <T : Any?> getArgument(index: Int): T {
        throw NotImplementedError()
    }

    override fun stubInfo(): StubInfo {
        throw NotImplementedError()
    }

    override fun getRawArguments(): Array<Any> = wrappedMethodInvocation.arguments

}