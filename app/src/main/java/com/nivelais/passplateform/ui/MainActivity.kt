package com.nivelais.passplateform.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.*
import com.nivelais.passplateform.App
import com.nivelais.passplateform.BuildConfig
import com.nivelais.passplateform.R
import com.nivelais.passplateform.data.local.entities.PassDatabase
import com.nivelais.passplateform.ui.opendb.OpenDbFragment
import com.nivelais.passplateform.utils.adapters.RecentlyOpennedAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // Define and load the view vm
    private val vm by lazy {
        ViewModelProviders.of(this)[MainViewModel::class.java]
    }

    // Define the scene of this view
    private lateinit var startScene: Scene
    private lateinit var fragmentScene: Scene

    // All the other ui component relative to this activity
    private lateinit var recentDatabasesView: RecyclerView
    private lateinit var recentTextView: TextView
    private lateinit var openDbButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set version text
        findViewById<TextView>(R.id.text_view_app_version).text =
            BuildConfig.VERSION_NAME

        // Create ur scene
        createScene()

        // Init recycler view
        recentDatabasesView.layoutManager = LinearLayoutManager(this)
        recentDatabasesView.adapter = RecentlyOpennedAdapter(ArrayList(), this)

        // Observe this activity state
        vm.getState().observe(this, stateObserver())

        // Observe databases live data
        vm.getDatabases().observe(this, databasesObserver())

        // Click on open db button
        openDbButton.setOnClickListener { vm.clickOpenDb() }
    }

    override fun onBackPressed() {
        vm.clickBack()
    }

    /**
     * Create ur scene and fetch ur ui component
     */
    private fun createScene() {
        // Create the scene
        val startSceneView = layoutInflater.inflate(R.layout.scene_main_start, layout_scene_container, false)
        val fragmentSceneView = layoutInflater.inflate(R.layout.scene_main_fragment, layout_scene_container, false)
        startScene = Scene(layout_scene_container, startSceneView)
        fragmentScene = Scene(layout_scene_container, fragmentSceneView)
        startScene.enter()

        // Fetch scene related component
        recentDatabasesView = startSceneView.findViewById(R.id.recycler_view_recently_open)
        recentTextView = startSceneView.findViewById(R.id.text_view_recently_open)
        openDbButton = startSceneView.findViewById(R.id.button_open_db)
    }

    /**
     * Observer for this activity state
     */
    private fun stateObserver() =
        Observer<MainViewModel.State> { state ->
            Log.d(App.TAG, "Received a new state : $state")

            when (state) {
                MainViewModel.State.START -> {
                    TransitionManager.go(startScene, getTransition())
                }
                MainViewModel.State.PROVIDER -> {
                    supportFragmentManager.beginTransaction()
                        .add(
                            R.id.layout_open_db_fragment,
                            OpenDbFragment.newInstance()
                        )
                        .commit()
                    TransitionManager.go(fragmentScene, getTransition())
                }
                else -> {
                    finish()
                }
            }
        }

    /**
     * Observer for new recently openned databases
     */
    private fun databasesObserver() =
        Observer<List<PassDatabase>> { databases ->
            Log.d(App.TAG, "Added new database to recycler view : $databases")

            // Change text view
            if (databases.isEmpty())
                recentTextView.text = getString(R.string.lbl_recently_open_none)
            else
                recentTextView.text = getString(R.string.lbl_recently_open)

            // Add list to adapter
            val adapter = (recentDatabasesView.adapter as RecentlyOpennedAdapter)
            adapter.databases.addAll(databases)
            adapter.notifyItemInserted(adapter.databases.size - 1)
        }

    /**
     * Get a slide down transition
     */
    private fun getTransition(): Transition {
        val titleSlide = Slide()
            .addTarget(R.id.text_view_app_title)
        val fragmentSlide = Slide()
            .addTarget(R.id.layout_open_db_fragment)
        val startSlide = Slide()
            .addTarget(R.id.layout_open_db_start)

        val bounds = ChangeBounds()
        bounds.addTarget(R.id.text_view_app_title)

        return TransitionSet()
            .setOrdering(TransitionSet.ORDERING_TOGETHER)
            .addTransition(startSlide)
            .addTransition(titleSlide)
            .addTransition(fragmentSlide)
            .addTransition(bounds)
            .setDuration(500)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.e(App.TAG, "intent result received")
        super.onActivityResult(requestCode, resultCode, data)
    }
}
