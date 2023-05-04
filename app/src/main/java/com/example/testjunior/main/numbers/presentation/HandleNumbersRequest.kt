package com.example.testjunior.main.numbers.presentation


import com.example.testjunior.main.numbers.domain.NumbersResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface HandleNumbersRequest {

    fun handle(
        coroutineScope: CoroutineScope,
        block: suspend () -> NumbersResult
    )

    class Base(
        private val communication: NumbersCommunication,
        private val dispatchers: DispatchersList,
        private val numberResultMapper: NumbersResult.Mapper<Unit>
    ) : HandleNumbersRequest {
        override fun handle(
            coroutineScope: CoroutineScope,
            block: suspend () -> NumbersResult) {

            communication.showProgress(true)
            coroutineScope.launch(dispatchers.io()) {
                val result = block.invoke()
                communication.showProgress(false)
                result.map(numberResultMapper)
            }

        }
    }
}