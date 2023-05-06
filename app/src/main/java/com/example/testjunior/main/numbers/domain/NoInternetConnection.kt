package com.example.testjunior.main.numbers.domain

abstract class DomainException : IllegalStateException()

class NoInternetConnectionException : DomainException()

class ServiceIsUnavailable : DomainException()
