package com.example.testjunior.main.numbers.presentation

sealed class UiState {

   //todo

    class Success() : UiState() {


        override fun equals(other: Any?): Boolean {
            return if(other is Success) true else super.equals(other)
        }
    }

  data  class Error(private val errorMessage: String) : UiState() {

    }
}