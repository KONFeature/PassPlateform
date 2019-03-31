package com.nivelais.passplateform.ui.opendb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nivelais.passplateform.data.local.entities.PassDatabase
import com.nivelais.passplateform.data.repositories.PassDatabaseRepository

class OpenDbViewModel: ViewModel() {

    // The current activity state
    private val state = MutableLiveData<State>(State.START)

    // Last openned password databases
    private val databases = MutableLiveData<List<PassDatabase>>()

    /**
     * Return the current state of the activity
     */
    fun getState(): LiveData<State> = state

    /**
     * Fetch the latest open pass database
     */
    fun getDatabases() : LiveData<List<PassDatabase>> {
        // Init if it wasn't done yet
        databases.value?:let{
            databases.postValue(PassDatabaseRepository.getRecentlyOpenned())
        }

        return databases
    }

    /**
     * Called when a user click on open database btn
     */
    fun clickOpenDb() {
        state.value?.let { currentState ->
            if(currentState == State.START) state.postValue(State.PROVIDER)
        }
    }

    /**
     * Called when a user press back
     */
    fun clickBack() {
        if(state.value != State.START)
            state.postValue(State.START)
        else
            state.postValue(State.EXIT)
    }

    enum class State {
        START,
        PROVIDER,
        EXPLORER,
        EXIT
    }

}