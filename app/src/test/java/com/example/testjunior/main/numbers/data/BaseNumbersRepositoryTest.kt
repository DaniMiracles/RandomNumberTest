package com.example.testjunior.main.numbers.data


import com.example.testjunior.main.numbers.domain.NoInternetConnectionException
import com.example.testjunior.main.numbers.domain.NumberFact
import com.example.testjunior.main.numbers.domain.NumbersRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

class BaseNumbersRepositoryTest {

    private lateinit var repository: NumbersRepository
    private lateinit var cloudDataSource: TestNumbersCloudDataSource
    private lateinit var cacheDataSource: TestNumbersCacheDataSource

    @Before
    fun setUp() {
        cloudDataSource = TestNumbersCloudDataSource()
        cacheDataSource = TestNumbersCacheDataSource()
        val mapper = NumberDataToDomain()

        repository = BaseNumbersRepository(
            cloudDataSource,
            cacheDataSource,
            HandleDataRequest.Base(mapper, HandleDomainError(), cacheDataSource),
            mapper
        )

    }

    @Test
    fun test_all_numbers() = runBlocking {

        cacheDataSource.replaceData(
            listOf(
                NumberData("4", "fact about 4"),
                NumberData("5", "fact about 5")
            )
        )


        val actual = repository.allNumbers()
        val expected = listOf(
            NumberFact("4", "fact about 4"),
            NumberFact("5", "fact about 5")
        )

        actual.forEachIndexed { index, item ->
            assertEquals(expected[index], item)
        }
        assertEquals(1, cacheDataSource.allNumbersCalledCount)
    }

    @Test
    fun test_number_fact_not_cached_success() = runBlocking {
        cloudDataSource.makeExpected(NumberData("10", "cloud of 10"))
        cacheDataSource.replaceData(emptyList())


        val actual = repository.numberFact("10")
        val expected = NumberFact("10", "cloud of 10")

        assertEquals(expected, actual)
        assertEquals(false, cacheDataSource.containsCalledList[0])
        assertEquals(1, cacheDataSource.containsCalledList.size)
        assertEquals(0, cacheDataSource.numberFactCalled.size)
        assertEquals(1, cacheDataSource.saveNumberFactCalledCount)
        assertEquals(NumberData("10", "cloud of 10"), cacheDataSource.data[0])
    }

    @Test(expected = NoInternetConnectionException::class)
    fun test_number_fact_not_cached_failure(): Unit = runBlocking {
        cloudDataSource.changeConnection(false)
        cloudDataSource.makeExpected(NumberData("10", "cloud of 10"))

        repository.numberFact("10")
        assertEquals(false, cacheDataSource.containsCalledList[0])
        assertEquals(1, cacheDataSource.containsCalledList.size)
        assertEquals(1, cloudDataSource.numberFactCalledCount)
        assertEquals(0, cacheDataSource.numberFactCalled.size)
        assertEquals(0, cacheDataSource.saveNumberFactCalledCount)
    }

    @Test
    fun test_number_fact_cached_success() = runBlocking {

        cloudDataSource.changeConnection(true)
        cloudDataSource.makeExpected(NumberData("11", "cloud of 11"))
        cacheDataSource.replaceData(listOf(NumberData("11", "fact of 11")))

        val actual = repository.numberFact("11")
        val expected = NumberFact("11", "fact of 11")

        assertEquals(expected, actual)
        assertEquals(true, cacheDataSource.containsCalledList[0])
        assertEquals(1, cacheDataSource.containsCalledList.size)
        assertEquals(0, cloudDataSource.numberFactCalledCount)
        assertEquals(1, cacheDataSource.numberFactCalled.size)
        assertEquals(1, cacheDataSource.saveNumberFactCalledCount)

    }

    @Test
    fun test_random_number_fact_not_cached_success() = runBlocking {
        cloudDataSource.makeExpected(NumberData("10", "cloud of 10"))
        cacheDataSource.replaceData(emptyList())

        val actual = repository.randomNumberFact()
        val expected = NumberFact("10", "cloud of 10")

        assertEquals(expected, actual)

        assertEquals(1, cloudDataSource.randomNumberFactCalledCount)
        assertEquals(0, cacheDataSource.numberFactCalled.size)
        assertEquals(1, cacheDataSource.saveNumberFactCalledCount)
        assertEquals(NumberData("10", "cloud of 10"), cacheDataSource.data[0])

    }

    @Test(expected = NoInternetConnectionException::class)
    fun test_random_number_fact_not_cached_failure() = runBlocking {
        cloudDataSource.changeConnection(false)
        cacheDataSource.replaceData(emptyList())

        repository.randomNumberFact()

        assertEquals(1, cloudDataSource.randomNumberFactCalledCount)
        assertEquals(0, cacheDataSource.numberFactCalled.size)
        assertEquals(1, cacheDataSource.saveNumberFactCalledCount)
    }

    @Test
    fun test_random_number_fact_cached_success() = runBlocking {

        cloudDataSource.changeConnection(true)
        cloudDataSource.makeExpected(NumberData("11", "fact of 11"))
        cacheDataSource.replaceData(listOf(NumberData("11", "fact of 11")))

        val actual = repository.randomNumberFact()
        val expected = NumberFact("11", "fact of 11")

        assertEquals(expected, actual)
        assertEquals(1, cloudDataSource.randomNumberFactCalledCount)

        assertEquals(0, cacheDataSource.numberFactCalled.size)
        assertEquals(1, cacheDataSource.saveNumberFactCalledCount)
    }
}

private class TestNumbersCloudDataSource() : NumbersCloudDataSource {

    private var thereIsConnection = true
    private var numberData = NumberData("", "")
    var numberFactCalledCount = 0
    var randomNumberFactCalledCount = 0

    fun changeConnection(connection: Boolean) {
        thereIsConnection = connection
    }

    fun makeExpected(number: NumberData) {
        numberData = number
    }


    override suspend fun numberFact(number: String): NumberData {
        numberFactCalledCount++
        if (thereIsConnection)
            return numberData
        else
            throw UnknownHostException()
    }

    override suspend fun randomNumberFact(): NumberData {
        randomNumberFactCalledCount++
        if (thereIsConnection)
            return numberData
        else
            throw UnknownHostException()
    }
}

private class TestNumbersCacheDataSource() : NumbersCacheDataSource {

    var containsCalledList = mutableListOf<Boolean>()
    var numberFactCalled = mutableListOf<String>()
    var randomNumberFactCalled = mutableListOf<String>()
    val data = mutableListOf<NumberData>()
    var saveNumberFactCalledCount = 0
    var allNumbersCalledCount = 0

    fun replaceData(list: List<NumberData>) {
        data.clear()
        data.addAll(list)
    }

    override suspend fun contains(number: String): Boolean {
        val result = data.find { it.matches(number) } != null
        containsCalledList.add(result)
        return result
    }

    override suspend fun allNumbers(): List<NumberData> {
        allNumbersCalledCount++
        return data
    }

    override suspend fun numberFact(number: String): NumberData {
        numberFactCalled.add(number)
        return data[0]
    }

    override suspend fun saveNumberFact(numberData: NumberData) {
        saveNumberFactCalledCount++
        data.add(numberData)
    }
}