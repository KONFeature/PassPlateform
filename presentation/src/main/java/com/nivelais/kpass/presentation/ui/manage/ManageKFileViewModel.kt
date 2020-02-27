package com.nivelais.kpass.presentation.ui.manage

import android.util.Log
import androidx.lifecycle.ViewModel
import com.nivelais.kpass.domain.entities.KFileEntity
import com.nivelais.kpass.domain.usecases.manager.CreateKFileUseCase
import com.nivelais.kpass.domain.usecases.manager.ImportKFileUseCase
import com.nivelais.kpass.domain.usecases.explorer.OpenKFileUseCase
import com.nivelais.kpass.domain.usecases.manager.RemoveKFileUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class ManageKFileViewModel(
    private val createKFileUseCase: CreateKFileUseCase,
    private val importKFileUseCase: ImportKFileUseCase,
    private val removeKFileUseCase: RemoveKFileUseCase,
    private val openKFileUseCase: OpenKFileUseCase
) : ViewModel() {

    val TAG = this::class.java.simpleName

    /*
    * Job and context for coroutines
    */
    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    /**
     * Launch the creation of a new KFile
     */
    fun createKfile(name: String, pass: String, callback: ((KFileEntity) -> Unit)) {
        createKFileUseCase(
            scope,
            CreateKFileUseCase.Params(name, pass)
        ) {
            if (it.isSuccess()) {
                Log.i(TAG, "KFile created with success")
                callback(it.data!!)
            } else {
                Log.e(TAG, "Error creating kfile ${it.error}")
            }
        }
    }

    /**
     * Remove a kfile
     */
    fun removeKfile(kfile: KFileEntity, removeFileToo: Boolean) {
        removeKFileUseCase(
            scope,
            RemoveKFileUseCase.Params(kfile, removeFileToo)
        ) {
            if (it.isSuccess()) {
                Log.i(TAG, "KFile removed with success")
            } else {
                Log.e(TAG, "Error removing kfile ${it.error}")
            }
        }
    }

    /**
     * Try to open a kfile
     */
    fun openKfile(kfile: KFileEntity, pass: String, callback: ((KFileEntity) -> Unit)) {
        openKFileUseCase(
            scope,
            OpenKFileUseCase.Params(kfile, pass)
        ) {
            if (it.isSuccess()) {
                Log.i(TAG, "KFile openned with success")
                callback(it.data!!)
            } else {
                Log.e(TAG, "Error openning kfile ${it.error}")
            }
        }
    }
}
