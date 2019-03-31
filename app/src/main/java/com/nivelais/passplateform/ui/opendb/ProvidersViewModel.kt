package com.nivelais.passplateform.ui.opendb

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nivelais.passplateform.App
import com.nivelais.passplateform.utils.Provider

class ProvidersViewModel: ViewModel() {

    // Live data of all available providers
    private val providers = MutableLiveData<List<Provider>>()

    // Live data representing an intent to launch
    val intentToLaunch = MutableLiveData<Intent>()

    /**
     * Function used to get the providers from the view
     */
    fun getProviders() : LiveData<List<Provider>> {
        providers.value?:let {
            providers.postValue(Provider.values().toList())
        }

        return providers
    }

    /**
     * Function called when a user as choosen a provider
     */
    fun providerChoosen(provider: Provider) {
        Log.d(App.TAG, "Provider clicked $provider")
        when(provider) {
            Provider.FILE_SYSTEM -> {
                // Post an intent that while open the file system selector and list all opennable file
                intentToLaunch.postValue(
                    Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "*/*"
                    })
            }
            Provider.GDRIVE -> {

            }
            else -> {
                Log.w(App.TAG, "Unknown provider choosen")
            }
        }
    }



}