package com.nivelais.kpass.presentation.ui.manage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nivelais.kpass.presentation.R
import kotlinx.android.synthetic.main.dialog_open_kfile.*
import kotlinx.android.synthetic.main.main_activity.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class OpenKFileDialog : BottomSheetDialogFragment() {

    /**
     * Import the view model
     */
    private val viewModel: ManageKFileViewModel by sharedViewModel()

    /**
     * The arguments for this dialog
     */
    val args: OpenKFileDialogArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_open_kfile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load the kfile name in the view
        txt_dialog_open_title.text = getString(R.string.title_dialog_open, args.kfile.name)

        // Listen to ui action
        btn_import_validate.setOnClickListener {
            viewModel.openKfile(args.kfile, text_input_kfile_password.editText?.text.toString()) {
                // Go the the main activity if it was a success
                nav_host_fragment.findNavController().navigate(
                    OpenKFileDialogDirections.actionOpenKFileDialogToMainActivity(it)
                )
            }
        }
    }
}