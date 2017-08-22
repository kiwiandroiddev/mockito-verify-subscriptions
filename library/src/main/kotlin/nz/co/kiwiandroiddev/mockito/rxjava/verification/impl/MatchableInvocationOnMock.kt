package nz.co.kiwiandroiddev.mockito.rxjava.verification.impl

import org.mockito.invocation.Invocation
import org.mockito.invocation.InvocationOnMock
import org.mockito.invocation.Location
import org.mockito.invocation.StubInfo
import java.lang.reflect.Method

class MatchableInvocationOnMock(val invocationOnMock: InvocationOnMock) : Invocation {

    override fun getArguments(): Array<Any> = invocationOnMock.arguments

    override fun getLocation(): Location {
        throw NotImplementedError()
    }

    override fun isIgnoredForVerification(): Boolean {
        throw NotImplementedError()
    }

    override fun getMethod(): Method = invocationOnMock.method

    override fun ignoreForVerification() {
    }

    override fun getSequenceNumber(): Int {
        throw NotImplementedError()
    }

    override fun getRawReturnType(): Class<*> {
        throw NotImplementedError()
    }

    override fun isVerified(): Boolean {
        throw NotImplementedError()
    }

    override fun getMock(): Any = invocationOnMock.mock

    override fun markVerified() {
    }

    override fun markStubbed(stubInfo: StubInfo?) {
        throw NotImplementedError()
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

    override fun getRawArguments(): Array<Any> = arguments

}