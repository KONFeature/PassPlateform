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
    }
}
