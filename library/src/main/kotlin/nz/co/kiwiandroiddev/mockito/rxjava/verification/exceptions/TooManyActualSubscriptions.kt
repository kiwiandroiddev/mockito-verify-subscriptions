package nz.co.kiwiandroiddev.mockito.rxjava.verification.exceptions

import org.mockito.exceptions.base.MockitoException

data class TooManyActualSubscriptions(override val message: String) : MockitoException(message)