package nz.co.kiwiandroiddev.mockito.rxjava.verification

import nz.co.kiwiandroiddev.mockito.rxjava.verification.exceptions.TooLittleActualSubscriptions
import nz.co.kiwiandroiddev.mockito.rxjava.verification.exceptions.TooManyActualSubscriptions
import nz.co.kiwiandroiddev.mockito.rxjava.verification.exceptions.WantedButNotSubscribedTo
import nz.co.kiwiandroiddev.mockito.rxjava.verification.impl.MatchableInvocationOnMock
import nz.co.kiwiandroiddev.mockito.rxjava.verification.impl.SubscriptionOnDefaultMockedObservableEvent
import org.mockito.internal.verification.api.VerificationData
import org.mockito.verification.VerificationMode

internal class SubscribedTo(val timesWanted: Int = 1) : VerificationMode {

    override fun verify(data: VerificationData) {
        val allInvocations = data.allInvocations
        val matchingSubscriptions = allInvocations
                .filter { it is SubscriptionOnDefaultMockedObservableEvent }
                .filter { candidate ->
                    val underlyingRealInvocation = MatchableInvocationOnMock(
                            (candidate as SubscriptionOnDefaultMockedObservableEvent).wrappedMethodInvocation
                    )
                    data.target.matches(underlyingRealInvocation)
                }

        when {
            matchingSubscriptions.isEmpty() && timesWanted > 0 ->
                throw notSubscribedTo()

            matchingSubscriptions.size > timesWanted ->
                throw tooManySubscriptions(actualCount = matchingSubscriptions.size)

            matchingSubscriptions.size < timesWanted ->
                throw tooLittleSubscriptions(actualCount = matchingSubscriptions.size)
        }
    }

    private fun notSubscribedTo() = WantedButNotSubscribedTo("not subscribed to")

    private fun tooManySubscriptions(actualCount: Int) =
            TooManyActualSubscriptions("too many subscriptions (subscribed to $actualCount times)")

    private fun tooLittleSubscriptions(actualCount: Int) =
            TooLittleActualSubscriptions("too few subscriptions (subscribed to $actualCount times)")

    override fun description(description: String): VerificationMode = this

}

fun wasSubscribedTo(times: Int = 1): VerificationMode = SubscribedTo(times)

fun neverSubscribedTo(): VerificationMode = SubscribedTo(timesWanted = 0)