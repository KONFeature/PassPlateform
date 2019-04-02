package com.nivelais.passplateform.ui.start

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import com.nivelais.passplateform.App
import com.nivelais.passplateform.data.local.entities.PassDatabase
import com.nivelais.passplateform.data.repositories.PassDatabaseRepository

class StartViewModel : ViewModel() {

    // Last openned password databases
    private val databases = ArrayList<PassDatabase>()

    /**
     * Fetch the latest open pass database
     */
    fun getDatabases() : ArrayList<PassDatabase> {
        // Init if it wasn't done yet
        if(databases.isEmpty()) {
            Log.d(App.TAG, "Adding new recently openned database")
            databases.addAll(PassDatabaseRepository.getRecentlyOpenned())
        }

        return databases
    }
}
