package com.example.testjunior.main.numbers.domain

interface NumbersInteractor {

     suspend fun init(): NumbersResult

     suspend fun factAboutNumber(number: String): NumbersResult

     suspend fun factAboutRandomNumber(): NumbersResult
}