package com.nivelais.passplateform.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.transition.*
import com.nivelais.passplateform.App
import com.nivelais.passplateform.BuildConfig
import com.nivelais.passplateform.ui.opendb.OpenDbFragment
import com.nivelais.passplateform.ui.start.StartFragment
import com.nivelais.passplateform.R
import com.nivelais.passplateform.ui.explorer.ExplorerFragment


class MainActivity : AppCompatActivity(),
    StartFragment.OnStartFragmentAction,
    OpenDbFragment.OnOpenDbFragmentAction {

    // Define and load the view vm
    private val vm by lazy {
        ViewModelProviders.of(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // If we don't have any instance yet
        savedInstanceState ?: let {
            // Set version text
            findViewById<TextView>(R.id.text_view_app_version).text = BuildConfig.VERSION_NAME

            // Launch start fragment
            supportFragmentManager
                .beginTransaction()
                .add(R.id.layout_main_fragment, StartFragment.newInstance())
                .commit()
        }
    }

    override fun onBackPressed() {
        // Little trick to send the onBackPressed info to fragment
        val currentFragment = supportFragmentManager.findFragmentById(R.id.layout_main_fragment)
        if(currentFragment is BackHandleFragment && currentFragment.onBackPressed())
            return

        // Else check if we need to go back to the start fragment, or call super
        if (currentFragment !is StartFragment)
            openFragment(StartFragment.newInstance())
        else
            super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.e(App.TAG, "intent result received")
        super.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * Function used to open a fragment with an animation
     */
    private fun openFragment(fragment: Fragment) {
        val transitionDelay: Long = 500

        val transaction = supportFragmentManager.beginTransaction()
        val oldFragment = supportFragmentManager.findFragmentById(R.id.layout_main_fragment)

        // Fragment transition
        oldFragment?.exitTransition = Slide().setDuration(transitionDelay)
        fragment.enterTransition = Slide().setDuration(transitionDelay)

        // Shared elem transition
        fragment.sharedElementEnterTransition = TransitionInflater.from(this)
            .inflateTransition(android.R.transition.move)
            .setDuration(transitionDelay)
        oldFragment?.view?.findViewById<TextView>(R.id.text_view_app_title)
            ?.let { transaction.addSharedElement(it, it.transitionName) }

        // Launch transaction
        transaction
            .replace(R.id.layout_main_fragment, fragment)
            .commit()
    }

    /**
     * Callback of the start fragment, when the user click on the open db button
     */
    override fun onClickOpenDb() {
        openFragment(OpenDbFragment.newInstance())
    }

    /**
     * Callback of the start fragment, when the user click on a recently open db button
     */
    override fun onDbClicked(id: Long) {
        openFragment(ExplorerFragment.newInstance(id))
    }

    /**
     * Callback of the open db fragment, when the user have selected a good database
     */
    override fun onDbOpenned(id: Long) {
        openFragment(ExplorerFragment.newInstance(id))
    }
}
