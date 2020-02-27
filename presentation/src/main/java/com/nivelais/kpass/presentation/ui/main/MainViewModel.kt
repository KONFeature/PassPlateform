package com.nivelais.kpass.presentation.ui.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class MainViewModel() : ViewModel() {

    /*
    * Job and context for coroutines
    */
    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    /**
     * Live data pour le context courant de l'explorer
     */
//    val explorerLive = explorerRepository.currentFolderElements

    /**
     * Function called to open a kfile
     */
//    fun openKfile(kFile: KFile) {
//
//    }

}
