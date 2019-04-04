package com.nivelais.passplateform.ui.explorer

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.transition.*
import com.nivelais.passplateform.R
import com.nivelais.passplateform.data.local.entities.PassDatabase
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

    // Scene of this fragment
    private lateinit var loadingScene: Scene
    private lateinit var passwordScene: Scene
    private lateinit var entriesScene: Scene

    // Ui components
    private lateinit var entriesRecyclerView: RecyclerView
    private lateinit var textViewPassDbName: TextView
    private lateinit var textViewEntriesDbName: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_explorer, container, false)

        // Create the scene for this view
        createScenes(view.findViewById(R.id.layout_scene_container))

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

    /**
     * Create scenes used by this view
     */
    private fun createScenes(container: ViewGroup) {
        // Create views
        val loadingSceneView =  layoutInflater.inflate(R.layout.scene_explorer_loading, container, false)
        val passwordSceneView =  layoutInflater.inflate(R.layout.scene_explorer_enter_password, container, false)
        val entriesSceneView =  layoutInflater.inflate(R.layout.scene_explorer_entries, container, false)

        // Fetch ui components
        textViewPassDbName = passwordSceneView.findViewById(R.id.text_view_explorer_db_name)
        textViewEntriesDbName = entriesSceneView.findViewById(R.id.text_view_explorer_db_name)
        entriesRecyclerView = entriesSceneView.findViewById(R.id.recycler_view_entries)

        // Create scenes
        loadingScene = Scene(container, loadingSceneView)
        passwordScene = Scene(container, passwordSceneView)
        entriesScene = Scene(container, entriesSceneView)

        // Enter loading scene
        loadingScene.enter()
    }

    // Ur database observer
    private fun databaseObserver() =
            Observer<PassDatabase> {db ->
                textViewPassDbName.text = db.name
                textViewEntriesDbName.text = db.name
                Handler().postDelayed( {
                    TransitionManager.go(entriesScene, getTransition())
                }, 1500)
            }

    // Ur transition between the scene
    private fun getTransition() : Transition {
        val titleSlide = Slide().addTarget(R.id.text_view_app_title)
        val nameSlide = Slide().addTarget(R.id.text_view_explorer_db_name)
        val contentSlide = Slide().addTarget(R.id.recycler_view_entries)

        val bounds = ChangeBounds().addTarget(R.id.text_view_app_title)

        return TransitionSet()
            .setOrdering(TransitionSet.ORDERING_TOGETHER)
            .addTransition(titleSlide)
            .addTransition(bounds)
            .addTransition(nameSlide)
            .addTransition(contentSlide)
            .setDuration(500)
    }

    // TODO : Multiple scene (loading, password, explorer)

}