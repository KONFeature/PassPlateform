package com.nivelais.kpass.presentation

import android.app.Application
import com.nivelais.kpass.presentation.di.objectboxModule
import com.nivelais.kpass.presentation.di.repositoryModule
import com.nivelais.kpass.presentation.di.useCasesModule
import com.nivelais.kpass.presentation.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class PassApplication : Application() {

    override fun onCreate(){
        super.onCreate()

        // Init koin
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@PassApplication)
            modules(listOf(objectboxModule, repositoryModule, useCasesModule, viewModelModule))
        }
    }
}