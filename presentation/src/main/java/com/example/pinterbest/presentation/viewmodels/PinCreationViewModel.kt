package com.example.pinterbest.presentation.viewmodels

import android.graphics.Bitmap
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinterbest.domain.common.Result
import com.example.pinterbest.domain.entities.IdEntity
import com.example.pinterbest.domain.entities.PinInfo
import com.example.pinterbest.domain.usecases.PostPinUseCase
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PinCreationViewModel @Inject constructor(
    private val postPinUseCase: PostPinUseCase
) : ViewModel() {
    private val _state = MutableLiveData(false)
    val state: LiveData<Boolean> = _state

    private val _posting = SingleLiveEvent<IdEntity?>()
    val posting: SingleLiveEvent<IdEntity?> = _posting

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun postPin(title: EditText, description: EditText, bitmapImage: Bitmap) {
        viewModelScope.launch {
            postPinUseCase(
                PinInfo(
                    title = title.text.toString(),
                    description = description.text.toString(),
                    tags = null
                ),
                getByteArray(bitmapImage)
            ).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _posting.value = result.data
                        _state.value = false
                    }
                    is Result.Error -> {
                        _error.value = result.exception.message
                        _state.value = false
                    }
                    is Result.Loading -> {
                        _state.value = true
                    }
                }
            }
        }
    }

    private fun getByteArray(image: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, QUALITY, stream)
        return stream.toByteArray()
    }

    companion object {
        const val QUALITY = 80
    }
}
