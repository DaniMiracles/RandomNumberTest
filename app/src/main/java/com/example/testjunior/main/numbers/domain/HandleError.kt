package com.example.testjunior.main.numbers.domain

import com.example.testjunior.R
import com.example.testjunior.main.numbers.presentation.ManageResources

interface HandleError {

    fun handle(e: Exception): String

    class Base(private val manageResources: ManageResources) : HandleError {
        override fun handle(e: Exception): String = when (e) {
            is NoInternetConnectionException -> manageResources.addString(R.string.noInternetConnection)
            else -> manageResources.addString(R.string.serviceIsUnavailable)
        }
    }
}