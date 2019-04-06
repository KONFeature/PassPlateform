package com.nivelais.passplateform.ui.explorer

import android.util.Log
import androidx.core.net.toFile
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nivelais.passplateform.App
import com.nivelais.passplateform.data.local.entities.PassDatabase
import com.nivelais.passplateform.data.repositories.PassDatabaseRepository
import de.slackspace.openkeepass.KeePassDatabase
import de.slackspace.openkeepass.domain.Entry
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class ExplorerViewModel: ViewModel() {

    private var currentDatabase: PassDatabase? = null

    val stateLiveData = MutableLiveData<ExplorerState>(ExplorerState.loading())

    /**
     * Function used to load the current database in the view
     */
    fun loadDatabase(dbId: Long) {
        Log.d(App.TAG, "Loading the database with id $dbId")
        PassDatabaseRepository.findById(dbId)?.let { db ->
            Log.d(App.TAG, "Database loaded")
            currentDatabase = db
            stateLiveData.postValue(ExplorerState.password(db.name))
        }
    }

    /**
     * Function used to enter the database
     */
    fun enterPassword(pass: String) {
        stateLiveData.postValue(ExplorerState.loading())
        GlobalScope.launch {
            currentDatabase?.let {database ->
                // Try to open the database
                val dbFile = database.localPath.toFile()
                try {
                    val db = KeePassDatabase.getInstance(dbFile).openDatabase(pass)
                    stateLiveData.postValue(ExplorerState.entries(database.name, db.entries))
                } catch (e: Exception) {
                    Log.w(App.TAG, "Exception when reading the database ${e.message}")
                    stateLiveData.postValue(ExplorerState.passwordError(e.message, database.name))
                }
            }
        }
    }

    class ExplorerState(val step: Step,
                        val msg: String?,
                        val dbName: String?,
                        val entries: List<Entry>?) {
        enum class Step {
            LOADING,
            PASSWORD_ENTRY,
            ENTRIES
        }

        companion object {
            fun loading() = ExplorerState(Step.LOADING, null, null, null)

            fun password(name: String?) = ExplorerState(Step.PASSWORD_ENTRY, null, name, null)
            fun passwordError(msg: String?, name: String?) = ExplorerState(Step.PASSWORD_ENTRY, msg, name, null)

            fun entries(name: String?, entries: List<Entry>) = ExplorerState(Step.ENTRIES, null, name, entries)
        }

        fun isLoading() = step == Step.LOADING
    }
}