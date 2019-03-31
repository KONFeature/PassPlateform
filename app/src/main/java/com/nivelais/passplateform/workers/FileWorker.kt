package com.nivelais.passplateform.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

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
class FileWorker(ctx: Context, param: WorkerParameters): Worker(ctx, param) {

    override fun doWork(): Result {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}