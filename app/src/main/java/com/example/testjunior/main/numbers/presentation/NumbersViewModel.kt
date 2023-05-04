package com.example.testjunior.main.numbers.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testjunior.R

import com.example.testjunior.main.numbers.domain.NumbersInteractor

class NumbersViewModel(
    private val manageResources: ManageResources,
    private val communication: NumbersCommunication,
    private val interactor: NumbersInteractor,
    private val handleResult: HandleNumbersRequest
) : ViewModel(), FetchNumber, ObserveNumbers {

    override fun observeProgress(owner: LifecycleOwner, observer: Observer<Boolean>) =
        communication.observeProgress(owner, observer)

    override fun observeState(owner: LifecycleOwner, observer: Observer<UiState>) =
        communication.observeState(owner, observer)

    override fun observeList(owner: LifecycleOwner, observer: Observer<List<NumberUi>>) =
        communication.observeList(owner, observer)


    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            handleResult.handle(viewModelScope) {
                interactor.init()
            }
        }
    }

    override fun fetchRandomNumberFact() = handleResult.handle(viewModelScope) {
            interactor.factAboutRandomNumber()
        }

    override fun fetchNumberFact(number: String) {
        if (number.isEmpty()) {
            communication.showState(UiState.Error(manageResources.addString(R.string.empty_string_error_message)))
        } else {
            handleResult.handle(viewModelScope) {
                interactor.factAboutNumber(number)
            }
        }
    }


}

interface FetchNumber {

    fun init(isFirstRun: Boolean)

    fun fetchRandomNumberFact()

    fun fetchNumberFact(number: String)
}


