package com.nivelais.passplateform.ui.explorer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.transition.*
import com.google.android.material.textfield.TextInputLayout
import com.nivelais.passplateform.App
import com.nivelais.passplateform.R
import com.nivelais.passplateform.ui.BackHandleFragment
import com.nivelais.passplateform.ui.start.StartFragment
import com.nivelais.passplateform.utils.adapters.KeepassFileAdapter
import de.slackspace.openkeepass.domain.KeePassFileElement
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.scene_explorer_keepass_file.*

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
    private lateinit var fileScene: Scene

    // Ui components
    private lateinit var entriesRecyclerView: RecyclerView
    private lateinit var inputPasswordDatabase: TextInputLayout
    private lateinit var buttonPassword: Button
    private lateinit var textViewFileUsername: TextView
    private lateinit var textViewFilePassword: TextView
    private lateinit var textViewFileNotes: TextView

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
            entriesRecyclerView.adapter = KeepassFileAdapter(ArrayList(), ctx, { folderUuid ->
                vm.folderSelected(folderUuid)
            }, { fileUuid ->
                vm.fileSelected(fileUuid)
            })
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
        val fileSceneView = layoutInflater.inflate(R.layout.scene_explorer_keepass_file, container, false)

        // Fetch ui components
        inputPasswordDatabase = passwordSceneView.findViewById(R.id.input_database_password)
        buttonPassword = passwordSceneView.findViewById(R.id.btn_enter_password)
        entriesRecyclerView = entriesSceneView.findViewById(R.id.recycler_view_entries)
        textViewFileUsername = fileSceneView.findViewById(R.id.textview_file_username)
        textViewFilePassword = fileSceneView.findViewById(R.id.textview_file_password)
        textViewFileNotes = fileSceneView.findViewById(R.id.textview_file_notes)

        // Create scenes
        loadingScene = Scene(container, loadingSceneView)
        passwordScene = Scene(container, passwordSceneView)
        filesScene = Scene(container, entriesSceneView)
        fileScene = Scene(container, fileSceneView)

        // Enter loading scene
        loadingScene.enter()
    }

    // Ur state observer
    private fun stateObserver() =
        Observer<ExplorerViewModel.ExplorerState> { state ->
            when (state.step) {
                ExplorerViewModel.ExplorerState.Step.LOADING -> {
                    switchSene(loadingScene, null, null)
                }
                ExplorerViewModel.ExplorerState.Step.PASSWORD_ENTRY -> {
                    switchSene(passwordScene, state.dbName, null)
                    state.msg?.let { inputPasswordDatabase.error = it }
                }
                ExplorerViewModel.ExplorerState.Step.ENTRIES -> {
                    switchSene(filesScene, state.dbName, vm.getPath())
                }
                ExplorerViewModel.ExplorerState.Step.FILE -> {
                    // Load the file information
                    state.file?.let { file ->
                        switchSene(fileScene, state.dbName, vm.getPath() + file.title)
                        textViewFileUsername.text = file.username
                        textViewFilePassword.text = file.password
                        textViewFileNotes.text = file.notes
                    }
                }
            }
        }

    // Ur files observer
    private fun filesObserver() =
        Observer<List<KeePassFileElement>> { files ->
            if (vm.stateLiveData.value?.isFiles() == true) {
                (entriesRecyclerView.adapter as KeepassFileAdapter).clear()
                (entriesRecyclerView.adapter as KeepassFileAdapter).add(files)

                // Refresh the path
                currentScene?.sceneRoot?.children?.first()?.let { view ->
                    if (view is ViewGroup) {
                        // Set the name and the path if the textview exist
                        view.findViewById<TextView>(R.id.text_view_explorer_keepath_path)?.text = vm.getPath()
                    }
                }
            }
        }

    // Fucntion used to switch between scene
    private fun switchSene(scene: Scene, name: String?, path: String?) {
        if (currentScene == scene)
            return

        currentScene = scene
        TransitionManager.go(scene, getTransition())

        // Find the common view to all scene
        scene.sceneRoot.children.first().let { view ->
            if (view is ViewGroup) {
                // Set the name and the path if the textview exist
                view.findViewById<TextView>(R.id.text_view_explorer_db_name)?.text = name
                view.findViewById<TextView>(R.id.text_view_explorer_keepath_path)?.text = path
            }
        }
    }

    override fun onBackPressed(): Boolean {
        return vm.goBack()
    }

    // Ur transition between the scene
    private fun getTransition(): Transition {
        val titleSlide = Slide().addTarget(R.id.text_view_app_title)
        val contentSlide = Slide().addTarget(R.id.text_view_explorer_db_name)
            .addTarget(R.id.text_view_explorer_keepath_path)
            .addTarget(R.id.recycler_view_entries)
            .addTarget(R.id.layout_container_password)
            .addTarget(R.id.layout_container_file_field)

        val bounds = ChangeBounds().addTarget(R.id.text_view_app_title)
            .addTarget(R.id.text_view_explorer_db_name)
            .addTarget(R.id.text_view_explorer_keepath_path)

        return TransitionSet()
            .setOrdering(TransitionSet.ORDERING_TOGETHER)
            .addTransition(titleSlide)
            .addTransition(bounds)
            .addTransition(contentSlide)
            .setDuration(500)
    }
}