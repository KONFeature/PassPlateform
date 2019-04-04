package com.nivelais.passplateform.ui.explorer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nivelais.passplateform.App
import com.nivelais.passplateform.data.local.entities.PassDatabase
import com.nivelais.passplateform.data.repositories.PassDatabaseRepository

class ExplorerViewModel: ViewModel() {

    val databaseLiveData = MutableLiveData<PassDatabase>()

    fun loadDatabase(dbId: Long) {
        Log.d(App.TAG, "Loading the database with id $dbId")
        PassDatabaseRepository.findById(dbId)?.let { db ->
            Log.d(App.TAG, "Database loaded")
            databaseLiveData.postValue(db)
        }
    }

}