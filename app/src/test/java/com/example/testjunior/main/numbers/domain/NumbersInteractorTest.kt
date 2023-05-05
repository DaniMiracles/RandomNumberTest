package com.example.testjunior.main.numbers.domain

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class NumbersInteractorTest {

    private lateinit var repository: TestNumbersRepository
    private lateinit var interactor: NumbersInteractor


    @Before
    fun setUp() {
        repository = TestNumbersRepository()
        interactor = NumbersInteractor.Base(repository)
    }

    @Test
    fun test_init_success() = runBlocking {
        repository.changeExpectedList(emptyList())

        val actual = interactor.init()
        val expected = NumbersResult.Success(emptyList())

        assertEquals(expected, actual)
        assertEquals(1, repository.allNumbersCalledCount)
    }

    @Test
    fun test_fact_about_number_success() = runBlocking() {
        repository.changeExpectedFactOfNumber(NumberFact("7", "fact about 7"))

        val actual = interactor.factAboutNumber("7")
        val expected = NumbersResult.Success(listOf(NumberFact("7", "fact about 7")))

        assertEquals(expected, actual)
        assertEquals("7", repository.numberFactCalledList[0])
        assertEquals(1, repository.numberFactCalledList.size)
    }

    @Test
    fun test_fact_about_number_error() = runBlocking {
        repository.expectingErrorGetFact(true)
        val actual = interactor.factAboutNumber("7")
        val expected = NumbersResult.Failure("no internet connection")

        assertEquals(expected, actual)
        assertEquals("7", repository.numberFactCalledList[0])
        assertEquals(1, repository.numberFactCalledList.size)
    }

    @Test
    fun test_fact_about_random_number_success() = runBlocking() {
        repository.changeExpectedFactOfRandomNumber(NumberFact("7", "fact about 7"))

        val actual = interactor.factAboutRandomNumber()
        val expected = NumbersResult.Success(listOf(NumberFact("7", "fact about 7")))

        assertEquals(expected, actual)
        assertEquals(1, repository.randomNumberFactCalledList.size)
    }

    @Test
    fun test_fact_about_random_number_error() = runBlocking {
        repository.expectingErrorGetRandomFact(true)

        val actual = interactor.factAboutRandomNumber()
        val expected = NumbersResult.Failure("no internet connection")

        assertEquals(expected, actual)

        assertEquals(1, repository.randomNumberFactCalledList.size)
    }
}


private class TestNumbersRepository : NumbersRepository() {

    private var numberFact = NumberFact("", "")
    private val allNumbers = mutableListOf<NumberFact>()
    private var errorWhileNumberFact = false

    var allNumbersCalledCount = 0
    var numberFactCalledList = mutableListOf<String>()
    var randomNumberFactCalledList = mutableListOf<String>()

    fun changeExpectedList(list: List<NumberFact>) {
        allNumbers.clear()
        allNumbers.addAll(list)
    }

    fun changeExpectedFactOfNumber(numberFact: NumberFact) {
        this.numberFact = numberFact
    }

    fun changeExpectedFactOfRandomNumber(numberFact: NumberFact) {
        this.numberFact = numberFact
    }

    fun expectingErrorGetFact(error: Boolean) {
        errorWhileNumberFact = error
    }
    fun expectingErrorGetRandomFact(error: Boolean) {
        errorWhileNumberFact = error
    }

    override fun allNumbers(): List<NumberFact> {
        allNumbersCalledCount++
        return allNumbers
    }

    override fun numberFact(number: String): NumberFact {
        numberFactCalledList.add(number)
        if (errorWhileNumberFact) {
            throw NoInternetConnectionException()
        }
        return numberFact
    }

    override fun randomNumberFact() : NumberFact{
        randomNumberFactCalledList.add("")
        if (errorWhileNumberFact) {
            throw NoInternetConnectionException()
        }
        return numberFact
    }
}