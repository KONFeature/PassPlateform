package com.nivelais.passplateform.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nivelais.passplateform.data.local.entities.PassDatabase
import com.nivelais.passplateform.data.repositories.PassDatabaseRepository

class MainViewModel: ViewModel() {

    enum class State {
        START,
        PROVIDER,
        EXPLORER,
        EXIT
    }
}