package com.nivelais.kpass.presentation.ui.manage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.R
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nivelais.kpass.presentation.databinding.DialogCreateKfileBinding
import kotlinx.android.synthetic.main.launch_activity.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CreateKFileDialog : BottomSheetDialogFragment() {

    /**
     * Import the view model
     */
    private val viewModel: ManageKFileViewModel by sharedViewModel()

    /**
     * The view binding we will use
     */
    private lateinit var binding: DialogCreateKfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCreateKfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup ui
        binding.btnCreateAdvanced.isEnabled = false

        // When user click the create button
        binding.btnCreateValidate.setOnClickListener {
            viewModel.createKfile(
                binding.textInputCreateName.editText.toString(),
                binding.textInputCreatePassword.editText.toString()
            ) { kfile ->
                // Go to kfile created
                nav_host_fragment.findNavController().navigate(
                    CreateKFileDialogDirections.actionCreateKFileDialogToMainActivity(kfile)
                )
            }
        }
    }
}