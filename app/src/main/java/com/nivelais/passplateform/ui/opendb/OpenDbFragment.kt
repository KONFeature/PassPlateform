package com.nivelais.passplateform.ui.opendb

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
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
import com.nivelais.passplateform.workers.FileWorker
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

    // Listener for ur fragment callback
    private lateinit var listener: OnOpenDbFragmentAction

    // UI components
    private lateinit var providerRecyclerView: RecyclerView
    private lateinit var loadingFileWorker: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_opendb, container, false)

        // Load ui component
        providerRecyclerView = view.findViewById(R.id.recycler_view_providers)
        loadingFileWorker = view.findViewById(R.id.loading_file_worker)

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

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Attach listener to fragment
        if (context is OnOpenDbFragmentAction)
            listener = context
        else
            throw ClassCastException("Caller context of open db fragment must implement OnOpenDbFragmentAction")
    }

    /**
     * Return the observer for state of the file worker
     */
    private fun workerStateObserver() =
        Observer<WorkInfo> { workInfo ->
            // Hide progress bar when finished
            if (workInfo.state.isFinished)
                loadingFileWorker.visibility = View.INVISIBLE

            // Check the state of the file worker
            when (workInfo.state) {
                WorkInfo.State.SUCCEEDED -> {
                    Log.d(App.TAG, "File worker ended with success")
                    listener.onDbOpenned(workInfo.outputData.getLong(FileWorker.RESULT_DB_ID, 0))
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

        if (resultCode != Activity.RESULT_OK || resultData?.data == null) {
            // Check if we have a good result
            context?.alert(R.string.lbl_dialog_pick_file_error) { okButton {} }?.show()
        } else {
            // Launch and observe the worker
            WorkManager.getInstance()
                .getWorkInfoByIdLiveData(vm.launchFileWorker(resultData.data!!, Provider.FILE_SYSTEM))
                .observe(viewLifecycleOwner, workerStateObserver())

            // Show the progress bar
            loadingFileWorker.visibility = View.VISIBLE
        }
    }

    interface OnOpenDbFragmentAction {
        fun onDbOpenned(id: Long)
    }
}