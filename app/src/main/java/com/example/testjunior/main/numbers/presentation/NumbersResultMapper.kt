package com.example.testjunior.main.numbers.presentation

import com.example.testjunior.main.numbers.domain.NumberFact
import com.example.testjunior.main.numbers.domain.NumbersResult

class NumbersResultMapper(
    private val communication: NumbersCommunication,
    private val mapper: NumberFact.Mapper<NumberUi>
) : NumbersResult.Mapper<Unit> {

    override fun map(list: List<NumberFact>, errorMessage: String) {
        communication.showState(
            if (errorMessage.isEmpty()) {
                if (list.isNotEmpty()) {
                    communication.showList(list.map { it.map(mapper) })
                }
                UiState.Success()
            } else
                (UiState.Error(errorMessage))
        )
    }
}
