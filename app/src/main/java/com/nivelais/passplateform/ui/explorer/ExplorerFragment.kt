package com.nivelais.passplateform.ui.explorer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.transition.*
import com.google.android.material.textfield.TextInputLayout
import com.nivelais.passplateform.R
import com.nivelais.passplateform.ui.BackHandleFragment
import com.nivelais.passplateform.ui.start.StartFragment
import com.nivelais.passplateform.utils.adapters.KeepassFileAdapter
import de.slackspace.openkeepass.domain.KeePassFileElement
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator

class ExplorerFragment : Fragment(), BackHandleFragment {

    companion object {
        private val ARGS_DB_ID = "databaseId"

        fun newInstance(dbId: Long): ExplorerFragment {
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
    private var currentScene: Scene? = null
    private lateinit var loadingScene: Scene
    private lateinit var passwordScene: Scene
    private lateinit var filesScene: Scene

    // Ui components
    private lateinit var entriesRecyclerView: RecyclerView
    private lateinit var textViewPassDbName: TextView
    private lateinit var textViewFilesDbName: TextView
    private lateinit var textViewFilesPath: TextView
    private lateinit var inputPasswordDatabase: TextInputLayout
    private lateinit var buttonPassword: Button

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
            entriesRecyclerView.adapter = KeepassFileAdapter(ArrayList(), ctx) { folderUuid ->
                vm.folderSelected(folderUuid)
            }
        }

        // handle click on the password button
        buttonPassword.setOnClickListener {
            vm.enterPassword(inputPasswordDatabase.editText?.text.toString())
        }

        // Init observer
        vm.stateLiveData.observe(viewLifecycleOwner, stateObserver())
        vm.filesLiveData.observe(viewLifecycleOwner, filesObserver())

        // Ask the view model to load the database
        arguments?.getLong(ARGS_DB_ID)?.let { vm.loadDatabase(it) }

        return view
    }

    /**
     * Create scenes used by this view
     */
    private fun createScenes(container: ViewGroup) {
        // Create views
        val loadingSceneView = layoutInflater.inflate(R.layout.scene_explorer_loading, container, false)
        val passwordSceneView = layoutInflater.inflate(R.layout.scene_explorer_enter_password, container, false)
        val entriesSceneView = layoutInflater.inflate(R.layout.scene_explorer_keepass_files, container, false)

        // Fetch ui components
        textViewPassDbName = passwordSceneView.findViewById(R.id.text_view_explorer_db_name)
        inputPasswordDatabase = passwordSceneView.findViewById(R.id.input_database_password)
        buttonPassword = passwordSceneView.findViewById(R.id.btn_enter_password)
        textViewFilesDbName = entriesSceneView.findViewById(R.id.text_view_explorer_db_name)
        textViewFilesPath = entriesSceneView.findViewById(R.id.text_view_explorer_keepath_path)
        entriesRecyclerView = entriesSceneView.findViewById(R.id.recycler_view_entries)

        // Create scenes
        loadingScene = Scene(container, loadingSceneView)
        passwordScene = Scene(container, passwordSceneView)
        filesScene = Scene(container, entriesSceneView)

        // Enter loading scene
        loadingScene.enter()
    }

    // Ur state observer
    private fun stateObserver() =
        Observer<ExplorerViewModel.ExplorerState> { state ->
            when(state.step) {
                ExplorerViewModel.ExplorerState.Step.LOADING -> {
                    switchSene(loadingScene)
                }
                ExplorerViewModel.ExplorerState.Step.PASSWORD_ENTRY -> {
                    textViewPassDbName.text = state.dbName
                    state.msg?.let { inputPasswordDatabase.error = it }
                    switchSene(passwordScene)
                }
                ExplorerViewModel.ExplorerState.Step.ENTRIES -> {
                    textViewFilesDbName.text = state.dbName
                    switchSene(filesScene)
                }
            }
        }

    // Ur files observer
    private fun filesObserver() =
        Observer<List<KeePassFileElement>> { files ->
            (entriesRecyclerView.adapter as KeepassFileAdapter).clear()
            (entriesRecyclerView.adapter as KeepassFileAdapter).add(files)
            textViewFilesPath.text = vm.getPath()
        }

    // Fucntion used to switch between scene
    private fun switchSene(scene: Scene) {
        if(currentScene == scene)
            return

        currentScene = scene
        TransitionManager.go(scene, getTransition())
    }

    override fun onBackPressed(): Boolean {
        return vm.goBack()
    }


    // Ur transition between the scene
    private fun getTransition(): Transition {
        val titleSlide = Slide().addTarget(R.id.text_view_app_title)
        val contentSlide = Slide().addTarget(R.id.text_view_explorer_db_name)
            .addTarget(R.id.recycler_view_entries)
            .addTarget(R.id.layout_container_password)

        val bounds = ChangeBounds().addTarget(R.id.text_view_app_title)
            .addTarget(R.id.text_view_explorer_db_name)

        return TransitionSet()
            .setOrdering(TransitionSet.ORDERING_TOGETHER)
            .addTransition(titleSlide)
            .addTransition(bounds)
            .addTransition(contentSlide)
            .setDuration(500)
    }
}