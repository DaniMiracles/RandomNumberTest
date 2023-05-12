package com.example.testjunior.main.numbers.data

import com.example.testjunior.main.numbers.domain.HandleError
import com.example.testjunior.main.numbers.domain.NoInternetConnectionException
import com.example.testjunior.main.numbers.domain.ServiceIsUnavailable
import java.net.UnknownHostException

class HandleDomainError : HandleError<Exception> {
    override fun handle(e: Exception): Exception {
        return when (e) {
            is UnknownHostException -> NoInternetConnectionException()
            else -> ServiceIsUnavailable()
        }
    }
}