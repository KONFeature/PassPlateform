package com.nivelais.passplateform.ui.opendb

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.nivelais.passplateform.App
import com.nivelais.passplateform.R
import com.nivelais.passplateform.utils.Provider
import com.nivelais.passplateform.utils.adapters.ProviderAdapter
import org.jetbrains.anko.*

class OpenDbFragment : Fragment() {

    companion object {
        fun newInstance() = OpenDbFragment()

        private const val LIVE_DATA_POSTED_INTENT = 13
    }

    // Define and load the view vm
    private val vm by lazy {
        ViewModelProviders.of(this)[OpenDbViewModel::class.java]
    }

    // Recycler view for the provider
    private lateinit var providerRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_providers, container, false)
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

        // Observe the state of the view
        vm.state.observe(viewLifecycleOwner, stateObserver())
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
        Observer<Intent> { intent -> startActivityForResult(intent, LIVE_DATA_POSTED_INTENT) }

    /**
     * Return the observer for staet of this view
     */
    private fun stateObserver() =
        Observer<OpenDbViewModel.State> { state ->
            when(state) {
                OpenDbViewModel.State.ERROR -> {
                    context?.alert(R.string.lbl_dialog_pick_file_error){okButton {}}?.show()
                }
                OpenDbViewModel.State.WORKER_IP -> {
                    // Observe the state of the file worker
                    vm.workerId?.let { id ->
                        WorkManager.getInstance().getWorkInfoByIdLiveData(id).observe(viewLifecycleOwner, workerStateObserver())
                    }
                }
            }
        }

    /**
     * Return the observer for state of the file worker
     */
    private fun workerStateObserver() =
        Observer<WorkInfo> { workInfo ->
            Log.wtf(App.TAG, "Received new state 131313")
            when(workInfo.state) {
                WorkInfo.State.SUCCEEDED -> {
                    Log.d(App.TAG, "Success worker 131313")
                }
                WorkInfo.State.FAILED -> {
                    context?.alert(R.string.lbl_dialog_pick_file_error){okButton {}}?.show()
                }
                else -> {
                }
            }
        }

    /**
     * Received a response from an openned intent
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        if (requestCode == LIVE_DATA_POSTED_INTENT)
            vm.intentResult(resultCode, resultData)
        else
            super.onActivityResult(requestCode, resultCode, resultData)
    }
}