package nz.co.kiwiandroiddev.mockito.rxjava.verification.exceptions

import org.mockito.exceptions.base.MockitoAssertionError
import org.mockito.internal.util.StringUtil

data class WantedButNotSubscribedTo(override val message: String) : MockitoAssertionError(message) {
    override fun toString(): String {
        return StringUtil.removeFirstLine(super.toString())
    }
}