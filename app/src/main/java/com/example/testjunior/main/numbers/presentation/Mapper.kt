package com.example.testjunior.main.numbers.presentation



interface Mapper<R, S> {

    fun map(source: S): R

    interface Unit<S> : Mapper<kotlin.Unit, S>
}