package com.example.pinterbest.presentation.utilities

import android.net.Uri
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts

class ImagePicker(
    activityResultRegistry: ActivityResultRegistry,
    callback: (imageUri: Uri?) -> Unit
) {
    private val getContent = activityResultRegistry.register(
        REGISTER_KEY,
        ActivityResultContracts.GetContent(),
        callback
    )

    fun pickImage() {
        getContent.launch(MIMETYPE_IMAGES)
    }

    companion object {
        private const val REGISTER_KEY = "IMAGE_PICKER"
        private const val MIMETYPE_IMAGES = "image/*"
    }
}