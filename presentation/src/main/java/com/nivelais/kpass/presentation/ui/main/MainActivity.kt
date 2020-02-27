package com.nivelais.kpass.presentation.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.nivelais.kpass.presentation.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    /**
     * Import the view model
     */
    private val viewModel: MainViewModel by viewModel()

    /**
     * The arguments required to launch this activity
     */
    val args: MainActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // Transmit the kfile to the view model
        args.kfile
    }
}
