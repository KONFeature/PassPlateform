package com.nivelais.passplateform.ui.opendb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.nivelais.passplateform.R

// TODO : Abstract explorer fragment that list folder and file, used to explor in drive and in pass database
class ExplorerFragment: Fragment() {

    companion object {
        fun newInstance() = ExplorerFragment()
    }

    // Define and load the view vm
    private val vm by lazy {
        ViewModelProviders.of(this)[OpenDbViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_opendb_providers, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }
}