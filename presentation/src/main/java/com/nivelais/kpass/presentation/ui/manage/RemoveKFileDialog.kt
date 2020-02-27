package com.nivelais.kpass.presentation.ui.manage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nivelais.kpass.presentation.R
import kotlinx.android.synthetic.main.dialog_remove_kfile.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RemoveKFileDialog : BottomSheetDialogFragment() {

    /**
     * Import the view model
     */
    private val viewModel: ManageKFileViewModel by sharedViewModel()

    /**
     * The arguments for this dialog
     */
    val args: RemoveKFileDialogArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_remove_kfile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bind some elements to the view
        val kfile = args.kfile
        title_remove_dialog.text = getString(R.string.title_dialog_remove, kfile.name)
        txt_remove_location.text = getString(R.string.txt_dialog_remove_location, kfile.path)
        txt_remove_last_open.text =
            getString(R.string.txt_dialog_remove_last_openned, kfile.lastOpen)

        // Listen to ui action
        btn_remove_validate.setOnClickListener {
            viewModel.removeKfile(args.kfile, switch_remove_file.isChecked)
            this.dismiss()
        }
        btn_remove_cancel.setOnClickListener {
            this.dismiss()
        }
    }
}