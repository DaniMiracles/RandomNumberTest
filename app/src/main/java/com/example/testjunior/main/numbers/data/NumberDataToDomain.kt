package com.example.testjunior.main.numbers.data

import com.example.testjunior.main.numbers.domain.NumberFact

class NumberDataToDomain : NumberData.Mapper<NumberFact> {
    override fun map(id: String, fact: String): NumberFact = NumberFact(id, fact)
}