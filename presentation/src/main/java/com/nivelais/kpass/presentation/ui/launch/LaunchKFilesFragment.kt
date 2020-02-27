package com.nivelais.kpass.presentation.ui.launch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nivelais.kpass.presentation.R
import com.nivelais.kpass.domain.entities.KFileEntity
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.launch_activity.*
import kotlinx.android.synthetic.main.launch_kfiles_fragment.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LaunchKFilesFragment : Fragment() {

    /**
     * Import the view model
     */
    private val viewModel: LaunchViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.launch_kfiles_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Send the click event to the view model
        btn_create_db.setOnClickListener {
            nav_host_fragment.findNavController()
                .navigate(LaunchKFilesFragmentDirections.actionLaunchKFilesFragmentToCreateKFileDialog())
        }
        btn_import_db.setOnClickListener {
            nav_host_fragment.findNavController()
                .navigate(LaunchKFilesFragmentDirections.actionLaunchKFilesFragmentToImportKFileDialog())
        }

        // Observe when new kfiles are posted
        viewModel.kfilesLive.observe(viewLifecycleOwner, Observer { kfiles ->
            recycler_kfiles.adapter?.let {
                // Update the list if the adapter exist
                (it as LaunchKFileAdapter).updateKFiles(kfiles)
            } ?: run {
                initRecyclerView(kfiles)
            }
        })
    }

    /**
     * Initialise the recycler view
     */
    private fun initRecyclerView(kfiles: List<KFileEntity>) {
        // Item some basic infos
        recycler_kfiles.layoutManager = LinearLayoutManager(this.context)
        recycler_kfiles.itemAnimator = SlideInLeftAnimator()
        // Create the adapter
        recycler_kfiles.adapter = LaunchKFileAdapter(kfiles, { kfile ->
            // Open the dialog to unlock the database
            nav_host_fragment.findNavController()
                .navigate(
                    LaunchKFilesFragmentDirections.actionLaunchKFilesFragmentToOpenKFileDialog(
                        kfile
                    )
                )
        }, { kfile ->
            // Launch the remove dialog in case of a long click
            nav_host_fragment.findNavController().navigate(
                LaunchKFilesFragmentDirections.actionLaunchKFilesFragmentToRemoveKFileDialog(
                    kfile
                )
            )
        })
    }
}
