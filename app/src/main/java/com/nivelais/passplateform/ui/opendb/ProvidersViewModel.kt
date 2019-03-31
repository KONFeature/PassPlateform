package com.nivelais.passplateform.ui.opendb

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.lifecycle.*
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Operation
import androidx.work.WorkManager
import com.nivelais.passplateform.App
import com.nivelais.passplateform.utils.Provider
import com.nivelais.passplateform.workers.FileWorker
import java.util.*
import java.util.concurrent.Executor

class ProvidersViewModel: ViewModel() {

    // Live data of all available providers
    private val providers = MutableLiveData<List<Provider>>()

    // Live data representing an intent to launch
    val intentToLaunch = MutableLiveData<Intent>()

    // Live data representing the current state of the view
    val state = MutableLiveData<State>()

    // Current file worker id
    var workerId: UUID? = null

    /**
     * Function used to get the providers from the view
     */
    fun getProviders() : LiveData<List<Provider>> {
        providers.value?:let {
            providers.postValue(Provider.values().toList())
        }

        return providers
    }

    /**
     * Function called when a user as choosen a provider
     */
    fun providerChoosen(provider: Provider) {
        Log.d(App.TAG, "Provider clicked $provider")
        when(provider) {
            Provider.FILE_SYSTEM -> {
                // Post an intent that while open the file system selector and list all opennable file
                intentToLaunch.postValue(
                    Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "*/*"
                    })
            }
            Provider.GDRIVE -> {

            }
            else -> {
                Log.w(App.TAG, "Unknown provider choosen")
            }
        }
    }

    /**
     * When we get the result of ur launched intent
     */
    fun intentResult(resultCode: Int, resultData: Intent?) {
        if (resultCode != Activity.RESULT_OK || resultData?.data == null) {
            Log.w(App.TAG, "Error when picking a file, not blocking")
            state.postValue(State.ERROR)
        } else {
            // Launch the file worker
            Log.d(App.TAG, "Successfully picked a file with data : ${resultData.data}")
            val fileWorker = OneTimeWorkRequestBuilder<FileWorker>().build()
            workerId = fileWorker.id
            WorkManager.getInstance().enqueue(fileWorker)
            state.postValue(State.WORKER_IP)
        }
    }

    /**
     * The state of the app
     */
    enum class State {
        ERROR,
        WORKER_IP
    }
}