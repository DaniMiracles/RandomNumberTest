package com.example.testjunior.main.numbers.data

import com.example.testjunior.main.numbers.domain.HandleError
import com.example.testjunior.main.numbers.domain.NumberFact

interface HandleDataRequest {

    suspend fun handle(block: suspend () -> NumberData) : NumberFact

    class Base(
        private val mapperToDomain: NumberData.Mapper<NumberFact>,
        private val handleError: HandleError<Exception>,
        private val cacheDataSource: NumbersCacheDataSource
    ) : HandleDataRequest {
        override suspend fun handle(block: suspend () -> NumberData) : NumberFact {
         return try {
                val result = block.invoke()
                cacheDataSource.saveNumberFact(result)
                result.mapFromNumberDataTo(mapperToDomain)
            } catch (e: Exception) {
                throw handleError.handle(e)
            }
        }
    }
}