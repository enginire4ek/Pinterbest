package com.example.pinterbest.presentation

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.example.pinterbest.domain.entities.IdEntity
import com.example.pinterbest.presentation.common.getAppComponent
import com.example.pinterbest.presentation.databinding.FragmentPinCreationBinding
import com.example.pinterbest.presentation.utilities.ImagePicker
import com.example.pinterbest.presentation.utilities.ResourceProvider
import com.example.pinterbest.presentation.utilities.Validator
import com.example.pinterbest.presentation.viewmodels.PinCreationViewModel

class PinCreationFragment : Fragment() {
    private val appComponent by lazy {
        getAppComponent()
    }

    private var selectedImageUri: Uri? = null

    private var _binding: FragmentPinCreationBinding? = null
    private val binding get() = _binding!!

    lateinit var imagePicker: ImagePicker

    private val viewModel: PinCreationViewModel by viewModels {
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
                    viewModel.postPin(binding.titleBox, binding.descriptionBox, bitmap)
                } catch (e: ClassCastException) {
                    Toast.makeText(
                        requireActivity(),
                        ResourceProvider(resources).getString(R.string.error_bitmap_convert),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        initObservers()
    }

    private fun initObservers() {
        viewModel.posting.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                showSuccess(response)
            }
        }
        viewModel.error.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                showError(response)
            }
        }
        viewModel.state.observe(
            viewLifecycleOwner,
            { loading ->
                when (loading) {
                    true -> binding.progressBar.visibility = View.VISIBLE
                    false -> binding.progressBar.visibility = View.GONE
                }
            }
        )
    }

    private fun showSuccess(response: IdEntity) {
        Toast.makeText(
            requireActivity(),
            "Загружен пин с id ${response.id}",
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
