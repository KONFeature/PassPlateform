package com.nivelais.kpass.presentation.ui.launch

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nivelais.kpass.domain.entities.KFileEntity
import com.nivelais.kpass.domain.usecases.manager.ListKFilesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class LaunchViewModel(listKFilesUseCase: ListKFilesUseCase) : ViewModel() {

    val TAG = this::class.java.simpleName

    /*
    * Job and context for coroutines
    */
    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    /**
     * Live data for the list of kfile in database
     */
    val kfilesLive = MutableLiveData<List<KFileEntity>>()

    init {
        // Fetch all the known kfiles
        listKFilesUseCase.invoke(
            scope,
            Unit) { response ->
            if(response.isSuccess()) {
                kfilesLive.postValue(response.data!!)
            } else {
                Log.e(TAG, "Error when fetching kfiles ${response.error?.message}")
            }
        }
    }
}
