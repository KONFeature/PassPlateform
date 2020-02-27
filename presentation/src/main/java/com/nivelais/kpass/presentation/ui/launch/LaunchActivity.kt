package com.nivelais.kpass.presentation.ui.launch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nivelais.kpass.presentation.R
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Activity for the splash screen
 */
class LaunchActivity : AppCompatActivity() {

    /**
     * Import the view model
     */
    private val viewModel: LaunchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.launch_activity)
    }

}
