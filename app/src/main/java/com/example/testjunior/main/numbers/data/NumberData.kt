package com.example.testjunior.main.numbers.data

data class NumberData(private val id: String, private val fact: String) {


    interface Mapper<T> {
        fun map(id: String, fact: String): T
    }

    fun <T> mapFromNumberDataTo(mapper: Mapper<T>): T {
        return mapper.map(id, fact)
    }

    fun matches(number:String) = number == id
}