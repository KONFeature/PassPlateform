package com.nivelais.kpass.presentation.ui.manage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nivelais.kpass.presentation.R
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ImportKFileDialog : BottomSheetDialogFragment() {

    /**
     * Import the view model
     */
    private val viewModel: ManageKFileViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_import_kfile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Listen to ui action
    }
}