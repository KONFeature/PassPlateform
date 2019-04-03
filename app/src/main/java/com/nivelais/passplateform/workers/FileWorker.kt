package com.nivelais.passplateform.workers

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.nivelais.passplateform.App
import com.nivelais.passplateform.data.local.entities.PassDatabase
import com.nivelais.passplateform.utils.Provider
import java.io.*
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

        // Param key for this worker
        const val PARAM_INPUT_URI = "inputUri"
        const val PARAM_INPUT_PROVIDER = "inputProvider"

        // Column to read on file metadata
        const val COLUMN_NAME = OpenableColumns.DISPLAY_NAME
        const val COLUMN_MIME = "mime_type"

        // The required mime type
        val REQUIRED_MIME_TYPE = listOf("application/octet-stream", "application/x-keepass2", "application/x-keepass", "application/keepass")
    }

    override fun doWork(): Result {
        assertParam()?.let { return it }

        // Get file params
        val uri = Uri.parse(param.inputData.keyValueMap[PARAM_INPUT_URI] as String)
        val provider = Provider.fromHashcode(param.inputData.keyValueMap[PARAM_INPUT_PROVIDER] as Int)
        assertFileType(uri)?.let { return it }

        // Retreive the fileName
        val fileName = retreiveBaseFileName(uri)?:let { return Result.failure() }

        // Copy the file to local storage
        val uriLocal = copyToLocal(uri, fileName)?:let { return Result.failure() }

        // Save the file to pass database
        App.store.boxFor(PassDatabase::class.java).put(PassDatabase( fileName, uriLocal, uri, provider, Date()))

        // Return the result
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

    /**
     * Read the file metadata of the given uri
     */
    private fun assertFileType(uri: Uri): Result? {
        val cursor: Cursor? = ctx.contentResolver.query( uri, null, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                // Get the mime type
                val mimeType: String = it.getString(it.getColumnIndex(COLUMN_MIME))
                Log.d(App.TAG, "File mimetype $mimeType")
                if(!REQUIRED_MIME_TYPE.contains(mimeType))
                    return Result.failure()
            }
        }?: let {
            Log.e(App.TAG, "Unable to read file metadata")
            return Result.failure()
        }

        return null
    }

    /**
     * Read the file metadata of the given uri
     */
    private fun retreiveBaseFileName(uri: Uri): String? {
        val cursor: Cursor? = ctx.contentResolver.query( uri, null, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
//                return null
                return it.getString(it.getColumnIndex(COLUMN_NAME))
            }
        }
        return null
    }

    /**
     * Function used to copy the selected file to the application storage
     */
    private fun copyToLocal(uri: Uri, fileName: String): Uri? {
        var res: Uri? = null
        try {
            val fileDestination = File(ctx.filesDir, fileName)
            ctx.contentResolver.openInputStream(uri)?.use { inputStream ->
                ctx.contentResolver.openOutputStream(Uri.fromFile(fileDestination))?.use {outputStream ->
                    // Copy the file content to destination
                    val copiedByte = inputStream.copyTo(outputStream)
                    Log.i(App.TAG, "Writed $copiedByte byte to file destination")

                    if(copiedByte > 0)
                        return Uri.fromFile(fileDestination)
                }
            }
        } catch(e: IOException) {
            Log.e(App.TAG, "Error occured during file copying ${e.message}")
        }

        return res
    }

}