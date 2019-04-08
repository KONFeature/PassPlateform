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
import de.slackspace.openkeepass.domain.Group
import de.slackspace.openkeepass.domain.KeePassFile
import de.slackspace.openkeepass.domain.KeePassFileElement
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class ExplorerViewModel : ViewModel() {

    private var currentDatabase: PassDatabase? = null
    private var keePassDatabase: KeePassFile? = null
    private var filePath: ArrayList<UUID> = ArrayList()

    val stateLiveData = MutableLiveData<ExplorerState>(ExplorerState.loading())
    val filesLiveData = MutableLiveData<ArrayList<KeePassFileElement>>(ArrayList())

    /**
     * Function used to load the current database in the view
     */
    fun loadDatabase(dbId: Long) {
        Log.d(App.TAG, "Loading the database with id $dbId")
        currentDatabase ?: let {
            PassDatabaseRepository.findById(dbId)?.let { db ->
                Log.d(App.TAG, "Database loaded")
                currentDatabase = db
                stateLiveData.postValue(ExplorerState.password(db.name))
            }
        }
    }

    /**
     * Function used to enter the database
     */
    fun enterPassword(pass: String) {
        stateLiveData.postValue(ExplorerState.loading())
        GlobalScope.launch {
            currentDatabase?.let { database ->
                // Try to open the database
                val dbFile = database.localPath.toFile()
                try {
                    keePassDatabase = KeePassDatabase.getInstance(dbFile).openDatabase(pass)
                    stateLiveData.postValue(ExplorerState.entries(database.name))
                    folderSelected(keePassDatabase!!.root.uuid)
                } catch (e: Exception) {
                    Log.w(App.TAG, "Exception when reading the database ${e.message}")
                    stateLiveData.postValue(ExplorerState.passwordError(e.message, database.name))
                }
            }
        }
    }

    /**
     * When the user click on a group
     */
    fun folderSelected(groupId: UUID) {
        keePassDatabase?.let { db ->
            if(filePath.isEmpty() || filePath.last() != groupId)
                filePath.add(groupId)

            val group = if(groupId == db.root.uuid)
                db.root
            else
                db.getGroupByUUID(groupId)

            // Find the group and add the element to the files live data
            val files = ArrayList<KeePassFileElement>(group.groups)
            files.addAll(group.entries)
            filesLiveData.postValue(files)
        }
    }

    /**
     * When the user click on a file
     */
    fun fileSelected(fileId: UUID) {
        keePassDatabase?.let { db ->
            // Find the file correspondign to the entry and send it to the view
            val file = db.getEntryByUUID(fileId)
            stateLiveData.postValue(ExplorerState.file(currentDatabase?.name, file))
        }
    }

    // Function used to go back in ur database
    fun goBack(): Boolean {
        if(stateLiveData.value?.isFile() == true) {
            stateLiveData.postValue(ExplorerState.entries(currentDatabase?.name))
            return true
        } else if(stateLiveData.value?.isLoading() == true ||
            stateLiveData.value?.isPassword() == true || filePath.size <= 1) {
            return false
        }

        filePath.removeAt(filePath.size - 1)
        folderSelected(filePath.last())
        return true
    }

    // Function used to get the current path of ur explorer
    fun getPath() = filePath.joinToString(separator = "/", prefix = "/", postfix = "/") { groupId ->
        keePassDatabase?.getGroupByUUID(groupId)?.name?:"root"
    }

    /**
     * Class representing the state of the explorer view
     */
    class ExplorerState(
        val step: Step,
        val msg: String?,
        val dbName: String?,
        val file: Entry?
    ) {
        enum class Step {
            LOADING,
            PASSWORD_ENTRY,
            ENTRIES,
            FILE
        }

        companion object {
            fun loading() = ExplorerState(Step.LOADING, null, null, null)

            fun password(name: String?) = ExplorerState(Step.PASSWORD_ENTRY, null, name, null)
            fun passwordError(msg: String?, name: String?) = ExplorerState(Step.PASSWORD_ENTRY, msg, name, null)

            fun entries(name: String?) = ExplorerState(Step.ENTRIES, null, name, null)

            fun file(name: String?, file: Entry) = ExplorerState(Step.FILE, null, name, file)
        }

        fun isLoading() = step == Step.LOADING
        fun isPassword() = step == Step.PASSWORD_ENTRY
        fun isFiles() = step == Step.ENTRIES
        fun isFile() = step == Step.FILE
    }
}