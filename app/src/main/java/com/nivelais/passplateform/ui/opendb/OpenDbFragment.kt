package com.nivelais.passplateform.ui.opendb

import android.app.Activity
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
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import org.jetbrains.anko.*

class OpenDbFragment : Fragment() {

    companion object {
        fun newInstance() = OpenDbFragment()
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
        val view = inflater.inflate(R.layout.fragment_opendb, container, false)

        // Load ui component
        providerRecyclerView = view.findViewById(R.id.recycler_view_providers)

        // Init recycler view
        activity?.let { ctx ->
            providerRecyclerView.itemAnimator = SlideInLeftAnimator()
            providerRecyclerView.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            providerRecyclerView.adapter = ProviderAdapter(vm.getProviders(), ctx) { provider ->
                // Start the intent corresponding to the provider clicked
                vm.getIntentFromProvider(provider)?.let { intent ->
                    startActivityForResult(intent, provider.intentRequestCode)
                }
            }
        }


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Put callback here
    }

    /**
     * Return the observer for state of the file worker
     */
    private fun workerStateObserver() =
        Observer<WorkInfo> { workInfo ->
            when (workInfo.state) {
                WorkInfo.State.SUCCEEDED -> {
                    Log.d(App.TAG, "File worker ended with success")
                }
                WorkInfo.State.FAILED -> {
                    context?.alert(R.string.lbl_dialog_pick_file_error) { okButton {} }?.show()
                }
                else -> {
                    context?.toast("Work status ${workInfo.state}")
                }
            }
        }

    /**
     * Received a response from an openned intent
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        // If we received a result different from pick file intent we return super result
        if (!Provider.requestCodes().contains(requestCode))
            return super.onActivityResult(requestCode, resultCode, resultData)

        if (resultCode != Activity.RESULT_OK || resultData?.data == null)
        // Check if we have a good result
            context?.alert(R.string.lbl_dialog_pick_file_error) { okButton {} }?.show()
        else
        // Launch and observe the worker
            WorkManager.getInstance()
                .getWorkInfoByIdLiveData(vm.launchFileWorker(resultData.data!!, Provider.FILE_SYSTEM))
                .observe(viewLifecycleOwner, workerStateObserver())
    }
}