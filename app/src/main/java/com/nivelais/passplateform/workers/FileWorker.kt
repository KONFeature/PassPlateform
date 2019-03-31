package com.nivelais.passplateform.workers

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.nivelais.passplateform.App
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
class FileWorker(val ctx: Context,
                 val param: WorkerParameters): Worker(ctx, param) {

    companion object {
        const val TAG = "FileWroker"

        const val PARAM_INPUT_URI = "inputUri"
    }

    override fun doWork(): Result {
        assertParam()?.let { return it }

        val fileUri = param.inputData.keyValueMap[PARAM_INPUT_URI] as Uri
        return Result.success()
    }

    /**
     * function used to be sure we have all the required param
     */
    private fun assertParam(): Result? {
        if (!param.inputData.keyValueMap.containsKey(PARAM_INPUT_URI)) {
            Log.e(App.TAG, "Error when loading pass file, missing required param")
            return Result.failure()
        }

        // Check if the param are in the good format
        if(param.inputData.keyValueMap[PARAM_INPUT_URI] !is Uri) {
            Log.e(App.TAG, "Error when loading pass file, input file isn't an URI object")
            return Result.failure()
        }

        return null
    }

}