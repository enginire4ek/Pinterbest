package com.example.pinterbest.presentation.viewmodels

import android.graphics.Bitmap
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinterbest.domain.common.Result
import com.example.pinterbest.domain.entities.BoardsList
import com.example.pinterbest.domain.entities.IdEntity
import com.example.pinterbest.domain.entities.PinInfo
import com.example.pinterbest.domain.entities.Profile
import com.example.pinterbest.domain.usecases.GetProfileBoardsUseCase
import com.example.pinterbest.domain.usecases.GetProfileDetailsUseCase
import com.example.pinterbest.domain.usecases.PostPinUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class PinCreationViewModel @Inject constructor(
    private val postPinUseCase: PostPinUseCase,
    private val getProfileBoardsUseCase: GetProfileBoardsUseCase,
    private val getProfileDetailsUseCase: GetProfileDetailsUseCase
) : ViewModel() {
    private val _state = MutableLiveData(false)
    val state: LiveData<Boolean> = _state

    private val _posting = SingleLiveEvent<IdEntity?>()
    val posting: SingleLiveEvent<IdEntity?> = _posting

    private val _boards = SingleLiveEvent<BoardsList?>()
    val boards: SingleLiveEvent<BoardsList?> = _boards

    private val _profile = MutableLiveData<Profile?>()
    val profile: LiveData<Profile?> = _profile

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun postPin(title: EditText, description: EditText, bitmapImage: Bitmap, boardId: Int) {
        viewModelScope.launch {
            postPinUseCase(
                PinInfo(
                    title = title.text.toString(),
                    description = description.text.toString(),
                    tags = null,
                    boardID = boardId
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

    fun getProfileBoards(id: Int) {
        viewModelScope.launch {
            getProfileBoardsUseCase(id.toString()).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _boards.value = result.data
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

    fun getProfileDetails() {
        viewModelScope.launch {
            val profile = async {
                getProfileDetailsUseCase().collect { result ->
                    when (result) {
                        is Result.Success -> {
                            _profile.value = result.data
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
            profile.await()
        }
    }

    private fun getByteArray(image: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, QUALITY, stream)
        return stream.toByteArray()
    }

    companion object {
        const val QUALITY = 100
    }
}
