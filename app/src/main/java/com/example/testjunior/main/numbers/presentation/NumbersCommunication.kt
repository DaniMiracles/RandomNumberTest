package com.example.testjunior.main.numbers.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

interface NumbersCommunication : ObserveNumbers {

    fun showProgress(show: Boolean)
    fun showState(uiState: UiState)
    fun showList(list: List<NumberUi>)

    class Base(
        private val progress: ProgressCommunication,
        private val state: StateCommunication,
        private val numbersList: ListCommunication
    ) : NumbersCommunication {

        override fun showProgress(show: Boolean)  = progress.map(show)

        override fun showState(uiState: UiState) = state.map(uiState)

        override fun showList(list: List<NumberUi>) = numbersList.map(list)

        override fun observeProgress(owner: LifecycleOwner, observer: Observer<Boolean>) {
            progress.observe(owner, observer)
        }

        override fun observeState(owner: LifecycleOwner, observer: Observer<UiState>) {
            state.observe(owner, observer)
        }

        override fun observeList(owner: LifecycleOwner, observer: Observer<List<NumberUi>>) {
            numbersList.observe(owner, observer)
        }
    }
}

interface ObserveNumbers{
    fun observeProgress(owner: LifecycleOwner,observer: Observer<Boolean>)
    fun observeState(owner: LifecycleOwner,observer: Observer<UiState>)
    fun observeList(owner: LifecycleOwner,observer: Observer<List<NumberUi>>)
}

interface ProgressCommunication : Communication.Mutable<Boolean> {
    class Base() : Communication.Post<Boolean>(), ProgressCommunication
}

interface StateCommunication : Communication.Mutable<UiState> {
    class Base() : Communication.Post<UiState>(), StateCommunication
}

interface ListCommunication : Communication.Mutable<List<NumberUi>> {
    class Base() : Communication.Post<List<NumberUi>>(), ListCommunication
}

