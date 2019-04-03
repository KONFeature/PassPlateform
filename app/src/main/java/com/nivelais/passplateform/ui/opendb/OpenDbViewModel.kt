package com.nivelais.passplateform.ui.opendb

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.nivelais.passplateform.App
import com.nivelais.passplateform.utils.Provider
import com.nivelais.passplateform.workers.FileWorker
import com.nivelais.passplateform.workers.FileWorker.Companion.REQUIRED_MIME_TYPE
import java.util.*
import kotlin.collections.ArrayList

class OpenDbViewModel : ViewModel() {

    // Choosen provider
    private var choosenProvider: Provider? = null

    /**
     * Function used to get the providers from the view
     */
    fun getProviders(): ArrayList<Provider> =
        ArrayList(Provider.values().toList())

    /**
     * Get the intent to launch in function of the provider
     */
    fun getIntentFromProvider(provider: Provider): Intent? {
        choosenProvider = provider
        return when (provider) {
            Provider.FILE_SYSTEM -> {
                // Create an intent that open file system selector
                Log.d(App.TAG, "File system database provider")
                Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = REQUIRED_MIME_TYPE[0]
                    putExtra(Intent.EXTRA_MIME_TYPES, FileWorker.REQUIRED_MIME_TYPE.toTypedArray())
                }
            }
            else -> {
                Log.w(App.TAG, "Unknown provider choosen")
                return null
            }
        }
    }

    /**
     * Generate a file worker when the file has been choosen
     */
    fun launchFileWorker(uri: Uri, provider: Provider): UUID {
        val fileWorker = OneTimeWorkRequestBuilder<FileWorker>()
            .setInputData(
                Data.Builder().putAll(
                    mapOf(
                        FileWorker.PARAM_INPUT_PROVIDER to provider.hashCode(),
                        FileWorker.PARAM_INPUT_URI to uri.toString()
                    )
                ).build()
            ).build()
        WorkManager.getInstance().enqueue(fileWorker)
        return fileWorker.id
    }
}