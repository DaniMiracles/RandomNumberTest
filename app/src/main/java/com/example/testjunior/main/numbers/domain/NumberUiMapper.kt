package com.example.testjunior.main.numbers.domain

import com.example.testjunior.main.numbers.presentation.NumberUi

class NumberUiMapper : NumberFact.Mapper<NumberUi> {
    override fun map(id: String, fact: String): NumberUi = NumberUi(id, fact)
}