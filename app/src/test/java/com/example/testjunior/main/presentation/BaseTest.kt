package com.example.testjunior.main.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.testjunior.main.numbers.presentation.NumberUi
import com.example.testjunior.main.numbers.presentation.NumbersCommunication
import com.example.testjunior.main.numbers.presentation.UiState

abstract class BaseTest {

    protected class TestNumbersCommunication : NumbersCommunication {

        val progressCalledList = mutableListOf<Boolean>()
        val stateCalledList = mutableListOf<UiState>()
        var timesShowList = 0
        val numbersList = mutableListOf<NumberUi>()

        override fun showProgress(show: Boolean) {
            progressCalledList.add(show)
        }

        override fun showState(uiState: UiState) {
            stateCalledList.add(uiState)
        }

        override fun showList(list: List<NumberUi>) {
            timesShowList++
            numbersList.addAll(list)
        }

        override fun observeProgress(owner: LifecycleOwner, observer: Observer<Boolean>) = Unit

        override fun observeState(owner: LifecycleOwner, observer: Observer<UiState>) = Unit

        override fun observeList(owner: LifecycleOwner, observer: Observer<List<NumberUi>>) = Unit
    }
}