package com.nivelais.passplateform.ui.explorer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.nivelais.passplateform.R
import com.nivelais.passplateform.data.local.entities.PassDatabase
import com.nivelais.passplateform.ui.opendb.OpenDbFragment
import com.nivelais.passplateform.ui.opendb.OpenDbViewModel
import com.nivelais.passplateform.utils.adapters.ProviderAdapter
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator

class ExplorerFragment : Fragment() {

    companion object {
        private val ARGS_DB_ID = "databaseId"

        fun newInstance(dbId: Long) : ExplorerFragment {
            val fragment = ExplorerFragment()
            val bundle = Bundle()
            bundle.putLong(ARGS_DB_ID, dbId)
            fragment.arguments = bundle
            return fragment
        }
    }

    // Define and load the view vm
    private val vm by lazy {
        ViewModelProviders.of(this)[ExplorerViewModel::class.java]
    }

    // Ui components
    private lateinit var entriesRecyclerView: RecyclerView
    private lateinit var textViewDbName: TextView
    private lateinit var loadingDataLoad: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_explorer, container, false)

        // Load ui component
        entriesRecyclerView = view.findViewById(R.id.recycler_view_entries)
        textViewDbName = view.findViewById(R.id.text_view_explorer_db_name)
        loadingDataLoad = view.findViewById(R.id.loading_explorer_entries)

        // Init recycler view
        activity?.let { ctx ->
            entriesRecyclerView.itemAnimator = SlideInLeftAnimator()
            entriesRecyclerView.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
//            entriesRecyclerView.adapter = EntriesAdapter(vm.getEntries(), ctx)
        }

        // Init observer
        vm.databaseLiveData.observe(viewLifecycleOwner, databaseObserver())

        // Ask the view model to load the database
        arguments?.getLong(ARGS_DB_ID)?.let {vm.loadDatabase(it) }

        return view
    }

    private fun databaseObserver() =
            Observer<PassDatabase> {db ->
                textViewDbName.text = db.name
                loadingDataLoad.visibility = View.GONE
                entriesRecyclerView.visibility = View.VISIBLE
            }

    // TODO : Multiple scene (loading, password, explorer)

}