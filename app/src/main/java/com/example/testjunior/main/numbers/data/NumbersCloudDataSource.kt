package com.example.testjunior.main.numbers.data



interface NumbersCloudDataSource : FetchNumber {

    suspend fun randomNumberFact(): NumberData

}