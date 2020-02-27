package com.nivelais.kpass.presentation.ui.main.explorer


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nivelais.kpass.domain.entities.EntryEntity
import com.nivelais.kpass.presentation.R
import com.nivelais.kpass.presentation.ui.main.MainViewModel
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.fragment_explorer_folder.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * A simple [Fragment] subclass.
 */
class ExplorerFolderFragment : Fragment() {

    /**
     * Import the view model
     */
    private val viewModel: MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explorer_folder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init recycler view / Ask to list entries

        // Listen to the livedata
        // TODO : Replace
//        viewModel.explorerLive.observe(viewLifecycleOwner) { entries ->
//            recyclerview_explorer_folder.adapter?.let {
//                // Update the list if the adapter exist
//                (it as ExplorerFolderAdapter).updateEntries(entries)
//            } ?: run {
//                initRecyclerView(entries)
//            }
//        }
    }

    /**
     * Initialise the recycler view
     */
    private fun initRecyclerView(entries: List<EntryEntity>) {
        // Item some basic infos
        recyclerview_explorer_folder.layoutManager = LinearLayoutManager(this.context)
        recyclerview_explorer_folder.itemAnimator = SlideInLeftAnimator()
        // Create the adapter
        recyclerview_explorer_folder.adapter = ExplorerFolderAdapter(entries)
    }

}
