package com.nivelais.passplateform.ui.opendb

import android.app.Activity
import android.content.ContentProvider
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.nivelais.passplateform.App
import com.nivelais.passplateform.R
import com.nivelais.passplateform.utils.Provider
import com.nivelais.passplateform.utils.adapters.ProviderAdapter
import org.jetbrains.anko.*

class ProvidersFragment : Fragment() {

    companion object {
        fun newInstance() = ProvidersFragment()

        private const val LIVE_DATA_POSTED_INTENT = 13
    }

    // Define and load the view vm
    private val vm by lazy {
        ViewModelProviders.of(this)[ProvidersViewModel::class.java]
    }

    // Recycler view for the provider
    private lateinit var providerRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_opendb_providers, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Init recycler view
        view?.let {
            providerRecyclerView = it.findViewById(R.id.recycler_view_providers)
            providerRecyclerView.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            providerRecyclerView.adapter = ProviderAdapter(ArrayList(), it.context) { provider ->
                vm.providerChoosen(provider)
            }
        } ?: kotlin.run {
            Log.e(App.TAG, "Failed to get the view from the provider fragment, exiting")
            fragmentManager?.beginTransaction()?.remove(this)?.commit()
        }

        // Load and observe the provider live data
        vm.getProviders().observe(viewLifecycleOwner, providerObserver())

        // Observe to see if we need to launch an intent
        vm.intentToLaunch.observe(viewLifecycleOwner, intentObserver())
    }

    /**
     * Return the observe for the provider live data
     */
    private fun providerObserver() =
        Observer<List<Provider>> { providers ->
            // Refresh ur provider view
            val adapter = (providerRecyclerView.adapter as ProviderAdapter)
            adapter.providers.addAll(providers)
            adapter.notifyItemInserted(adapter.providers.size - 1)
        }

    /**
     * Return the observer for new intent posted by the view model
     */
    private fun intentObserver() =
        Observer<Intent> { intent ->
            Log.wtf(App.TAG, "Launching intent")
            startActivityForResult(intent, LIVE_DATA_POSTED_INTENT)
        }

    /**
     * Received a response from an intent openned with start for result
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        if (requestCode == LIVE_DATA_POSTED_INTENT) {
            if (resultCode != Activity.RESULT_OK || resultData?.data?.encodedPath?.endsWith(".kdbx") != true) {
                Log.e(App.TAG, "Successfully picked a file with data : ${resultData?.data?.path}")
                showInvalidFileError()
                return
            } else {
                // TODO : view model call to the file worker
                Log.e(App.TAG, "Successfully picked a file with data : ${resultData.data}")
            }
        }

        super.onActivityResult(requestCode, resultCode, resultData)
    }

    /**
     * Show a dialog saying that the user pick an invalid file
     */
    private fun showInvalidFileError() {
        context?.let { ctx ->
            ctx.alert(R.string.lbl_dialog_pick_file_error) {
                yesButton {
                    vm.intentToLaunch.value?.let { intent ->
                        startActivityForResult(intent, LIVE_DATA_POSTED_INTENT)
                    } ?: kotlin.run {
                        ctx.toast("Error when relaunching the intent")
                    }
                }
                noButton {}
            }.show()
        }
    }
}