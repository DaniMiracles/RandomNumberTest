package com.example.testjunior.main.numbers.presentation

import com.example.testjunior.main.numbers.domain.NumberFact
import com.example.testjunior.main.numbers.domain.NumberUiMapper
import com.example.testjunior.main.presentation.BaseTest
import org.junit.Assert.*
import org.junit.Test

class NumbersResultMapperTest : BaseTest() {

    @Test
    fun test_error() {
        val communication = TestNumbersCommunication()
        val mapper = NumbersResultMapper(communication, NumberUiMapper())

        mapper.map(emptyList(), "not empty message")
        assertEquals(UiState.Error("not empty message"), communication.stateCalledList[0])
    }


    @Test
    fun test_success_emptyList(){
        val communication = TestNumbersCommunication()
        val mapper = NumbersResultMapper(communication, NumberUiMapper())

        mapper.map(emptyList(),"")
        assertEquals(0,communication.timesShowList)
        assertEquals(true, communication.stateCalledList[0] is UiState.Success)

    }

    @Test
    fun test_success_list(){
        val communication = TestNumbersCommunication()
        val mapper = NumbersResultMapper(communication, NumberUiMapper())

        mapper.map(listOf(NumberFact("5","fact is 5")),"")
        assertEquals(true, communication.stateCalledList[0] is UiState.Success)
        assertEquals(1,communication.timesShowList)
    }
}