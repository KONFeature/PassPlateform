package com.nivelais.passplateform

import android.app.Application
import android.util.Log
import com.nivelais.passplateform.data.local.entities.MyObjectBox
import com.nivelais.passplateform.data.local.entities.PassDatabase
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import java.util.*

/**
 * Class wrapping ur application wild reference
 */
class App : Application() {

    companion object Constants {
        // Tag for logging
        const val TAG = "PassPlateformApplication"

        // Object box store
        lateinit var store: BoxStore
    }


    override fun onCreate() {
        super.onCreate()

        // Init ur db
        Log.d(TAG, "Init app and db store")
        store = MyObjectBox.builder()
            .androidContext(applicationContext)
            .build()

        // TODO : To remove
        // Sample add item
        val box: Box<PassDatabase> = store.boxFor()
        if(box.all.isEmpty())
            box.put(
                PassDatabase("db1", "/db1.kdbx", "gdrive", Date()),
                PassDatabase("db2", "/db2.kdbx", "gdrive", Date()),
                PassDatabase("db3", "/db3.kdbx", "local", Date()),
                PassDatabase("db4", "/db4.kdbx", "dropbox", Date()),
                PassDatabase("db5", "/db5.kdbx", "gdrive", Date()),
                PassDatabase("db6", "/db6.kdbx", "local", Date())
            )
    }
}
