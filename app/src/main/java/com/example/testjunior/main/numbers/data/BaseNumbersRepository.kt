package com.example.testjunior.main.numbers.data

import com.example.testjunior.main.numbers.domain.NumberFact
import com.example.testjunior.main.numbers.domain.NumbersRepository

class BaseNumbersRepository(
    private val cloudDataSource: NumbersCloudDataSource,
    private val cacheDataSource: NumbersCacheDataSource,
    private val handleDataRequest: HandleDataRequest,
    private val mapperToDomain: NumberData.Mapper<NumberFact>
) : NumbersRepository {

    override suspend fun allNumbers(): List<NumberFact> {
        val data = cacheDataSource.allNumbers()
        return data.map { it.mapFromNumberDataTo(mapperToDomain) }
    }

    override suspend fun numberFact(number: String): NumberFact = handleDataRequest.handle {
        val dataSource = if (cacheDataSource.contains(number))
            cacheDataSource
        else
            cloudDataSource
        dataSource.numberFact(number)
    }

    override suspend fun randomNumberFact(): NumberFact = handleDataRequest.handle {
        cloudDataSource.randomNumberFact()
    }
}

