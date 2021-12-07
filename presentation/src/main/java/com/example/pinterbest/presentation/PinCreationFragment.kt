package com.example.pinterbest.presentation

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.pinterbest.presentation.common.getAppComponent
import com.example.pinterbest.presentation.utilities.ImagePicker
import com.example.pinterbest.presentation.databinding.FragmentPinCreationBinding
import com.example.pinterbest.presentation.utilities.ResourceProvider
import com.example.pinterbest.presentation.utilities.Validator
import com.example.pinterbest.presentation.viewmodels.PinCreationViewModel
import android.widget.Toast
import com.example.pinterbest.domain.entities.IdEntity
import java.io.File
import com.example.pinterbest.presentation.common.getFileName
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream


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
        inflater: LayoutInflater, container: ViewGroup?,
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
                val bitmap = (binding.loadedImage.drawable as BitmapDrawable).bitmap
                viewModel.postPin(binding.titleBox, binding.descriptionBox, bitmap)
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
        Toast.makeText(requireActivity(), "Загружен пин с id ${response.id}", Toast.LENGTH_SHORT).show()

    }

    private fun showError(response: String) {
        Toast.makeText(requireActivity(), response, Toast.LENGTH_SHORT).show()
    }
    /*private fun uploadImage(): File {
        val parcelFileDescriptor = (requireActivity() as MainActivity)
            .contentResolver.openFileDescriptor(
                selectedImageUri!!,
                "r",
                null
            )
        val inputStream = FileInputStream(parcelFileDescriptor?.fileDescriptor)
        val file = File(requireActivity().cacheDir, (requireActivity() as MainActivity)
            .contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        return file
    }*/

    /*private fun fileFromBitmap(filename: String):File {
        val f = File(requireContext().cacheDir, filename)
        f.createNewFile()
        val bitmap = (binding.loadedImage.drawable as BitmapDrawable).bitmap
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream) // bm is the bitmap object
        val bytes = byteArrayOutputStream.toByteArray()
        val fos = FileOutputStream(f)
        fos.write(bytes)
        fos.flush()
        fos.close()
        return f
    }*/

    /*private fun imageToByteArray(): ByteArray {
        val bitmap = (binding.loadedImage.drawable as BitmapDrawable).bitmap
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream) // bm is the bitmap object
        return byteArrayOutputStream.toByteArray()
    }*/

    /*private fun imageToString(): String {
        val bitmap = (binding.loadedImage.drawable as BitmapDrawable).bitmap
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream) // bm is the bitmap object
        val bytes = byteArrayOutputStream.toByteArray()
        val result = Base64.encodeToString(bytes, Base64.DEFAULT)
        return result
    }*/

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