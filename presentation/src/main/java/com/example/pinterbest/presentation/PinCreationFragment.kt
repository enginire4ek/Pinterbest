package com.example.pinterbest.presentation

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.pinterbest.domain.entities.BoardsList
import com.example.pinterbest.presentation.common.getAppComponent
import com.example.pinterbest.presentation.databinding.FragmentPinCreationBinding
import com.example.pinterbest.presentation.utilities.ImagePicker
import com.example.pinterbest.presentation.utilities.ResourceProvider
import com.example.pinterbest.presentation.utilities.Validator
import com.example.pinterbest.presentation.viewmodels.PinCreationViewModel
import com.example.pinterbest.presentation.viewmodels.ProfileViewModel

class PinCreationFragment : Fragment() {
    private val appComponent by lazy {
        getAppComponent()
    }

    private var selectedImageUri: Uri? = null
    private var profileId: Int? = null
    private var boardId: Int? = null

    private var _binding: FragmentPinCreationBinding? = null
    private val binding get() = _binding!!

    lateinit var imagePicker: ImagePicker

    private val viewModel: PinCreationViewModel by viewModels {
        appComponent.viewModelsFactory()
    }

    private val profileViewModel: ProfileViewModel by viewModels {
        appComponent.viewModelsFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPinCreationBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.getAuthStatus()
        initAuthObservers()

        binding.addImages.setOnClickListener {
            addImagesFromGallery()
        }

        binding.back.setOnClickListener {
            val navHostFragment = requireActivity().supportFragmentManager
                .findFragmentById(R.id.NavHostFragment) as NavHostFragment
            navHostFragment.navController.navigateUp()
        }

        imagePicker = ImagePicker(requireActivity().activityResultRegistry) { imageUri ->
            binding.loadedImage.visibility = View.VISIBLE
            selectedImageUri = imageUri ?: Uri.EMPTY
            Glide.with(view.context)
                .load(imageUri)
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.ic_error)
                .into(binding.loadedImage)
        }

        binding.uploadButton.setOnClickListener {
            if (validateUserFields()) {
                try {
                    val bitmap = (binding.loadedImage.drawable as BitmapDrawable).bitmap
                    if (boardId == null) {
                        showError(
                            ResourceProvider(resources).getString(R.string.error_board_not_selected)
                        )
                    } else {
                        viewModel.postPin(
                            binding.titleBox,
                            binding.descriptionBox,
                            bitmap,
                            boardId!!
                        )
                    }
                } catch (e: ClassCastException) {
                    showError(
                        ResourceProvider(resources).getString(R.string.error_bitmap_convert)
                    )
                }
            }
        }
    }

    private fun initAuthObservers() {
        profileViewModel.loggedIn.observe(viewLifecycleOwner) { loggedIn ->
            when (loggedIn) {
                true -> {
                    viewModel.getProfileDetails()
                    initObservers()
                }
                false -> {
                    val loginArgs = LoginFragmentArgs.Builder()
                    loginArgs.returnFragmentId = R.id.profileFragment
                    findNavController().navigate(R.id.loginFragment, loginArgs.build().toBundle())
                }
            }
        }
        profileViewModel.checkAuthError.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                showError(ResourceProvider(resources).getString(response))
            }
        }
        profileViewModel.checkAuthState.observe(viewLifecycleOwner) { loading ->
            when (loading) {
                true -> binding.progressBar.visibility = View.VISIBLE
                false -> binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun initObservers() {
        viewModel.run {
            posting.observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    showSuccess()
                }
            }
            profile.observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    profileId = response.id
                    viewModel.getProfileBoards(profileId!!)
                }
            }
            boards.observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    showDropDownMenu(response)
                }
            }
            error.observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    showError(response)
                }
            }
            state.observe(
                viewLifecycleOwner,
                { loading ->
                    when (loading) {
                        true -> binding.progressBar.visibility = View.VISIBLE
                        false -> binding.progressBar.visibility = View.GONE
                    }
                }
            )
        }
    }

    private fun showDropDownMenu(response: BoardsList) {
        val adapter = ArrayAdapter(requireContext(), R.layout.view_holder_board, response.boards)
        binding.dropdownList.setAdapter(adapter)
        binding.dropdownList.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            boardId = response.boards[position].ID
        }
    }

    private fun showSuccess() {
        Toast.makeText(
            requireActivity(),
            ResourceProvider(resources).getString(R.string.success_create_pin),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showError(response: String) {
        Toast.makeText(requireActivity(), response, Toast.LENGTH_SHORT).show()
    }

    private fun addImagesFromGallery() {
        imagePicker.pickImage()
    }

    private fun validateUserFields(): Boolean {
        return Validator(ResourceProvider(resources))
            .isValidName(binding.titleBox, true) &&
            Validator(ResourceProvider(resources))
                .isValidName(binding.descriptionBox, true) &&
            binding.loadedImage.visibility == View.VISIBLE
    }
}
