package com.example.testjunior.main.numbers.domain

interface NumbersRepository {

    suspend fun allNumbers(): List<NumberFact>

    suspend fun numberFact(number: String): NumberFact

    suspend fun randomNumberFact(): NumberFact
}