package com.nivelais.passplateform.workers

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.nivelais.passplateform.App
import com.nivelais.passplateform.utils.Provider
import java.util.*

/**
 * Worker for all the file operation
 *
 * TODO :
 *      -> Get a file from an URI
 *          -> Check if the file is writeable
 *          -> Check if it's a virtual uri or not
 *      -> Copy the file to app storage
 *      -> link the copied to the file with new object in db
 *      -> crypt the local file ?
 *      -> return local file
 */
class FileWorker(
    val ctx: Context,
    val param: WorkerParameters
) : Worker(ctx, param) {

    companion object {
        const val TAG = "FileWroker"

        const val PARAM_INPUT_URI = "inputUri"
        const val PARAM_INPUT_PROVIDER = "inputProvider"
    }

    override fun doWork(): Result {
        assertParam()?.let { return it }

        // Get file params
        val uri = Uri.parse(param.inputData.keyValueMap[PARAM_INPUT_URI] as String)
        val provider = Provider.fromHashcode(param.inputData.keyValueMap[PARAM_INPUT_PROVIDER] as Int)

        // Clone the file in different way, depending of the provider
        when(provider) {
            Provider.FILE_SYSTEM -> {

            }
            else -> {
                Log.w(App.TAG, "Provider not implemented yet")
                return Result.failure()
            }
        }

        Log.e(App.TAG, "Authority ? ${uri}, provider $provider")

        return Result.success()
    }

    /**
     * function used to be sure we have all the required param
     */
    private fun assertParam(): Result? {
        // Check if we have all the param
        if (!param.inputData.keyValueMap.containsKey(PARAM_INPUT_URI) ||
            !param.inputData.keyValueMap.containsKey(PARAM_INPUT_PROVIDER)
        ) {
            Log.e(App.TAG, "Error when loading pass file, missing required param")
            return Result.failure()
        }

        // Check if the param are in the good format
        if (param.inputData.keyValueMap[PARAM_INPUT_URI] !is String ||
            Uri.parse(param.inputData.keyValueMap[PARAM_INPUT_URI] as String) !is Uri) {
            Log.e(App.TAG, "Error when loading pass file, input file isn't an URI object")
            return Result.failure()
        }
        if (param.inputData.keyValueMap[PARAM_INPUT_PROVIDER] !is Int ||
                Provider.fromHashcode(param.inputData.keyValueMap[PARAM_INPUT_PROVIDER] as Int) == null) {
            Log.e(App.TAG, "Error when loading pass file, input file provider isn't a Provider object")
            return Result.failure()
        }

        return null
    }

}