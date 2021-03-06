package com.example.pinterbest.presentation.viewmodels

import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinterbest.domain.common.Result
import com.example.pinterbest.domain.entities.BoardCreation
import com.example.pinterbest.domain.entities.IdEntity
import com.example.pinterbest.domain.usecases.PostNewBoardUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class BoardCreationViewModel @Inject constructor(
    private val postNewBoardUseCase: PostNewBoardUseCase
) : ViewModel() {
    private val _loadingState = MutableLiveData(false)
    val loadingState: LiveData<Boolean> = _loadingState

    private val _board = SingleLiveEvent<IdEntity?>()
    val board: SingleLiveEvent<IdEntity?> = _board

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun postNewBoard(title: EditText, description: EditText) {
        viewModelScope.launch {
            postNewBoardUseCase(
                BoardCreation(
                    title.text.toString(),
                    description.text.toString()
                )
            ).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _board.value = result.data
                        _loadingState.value = false
                    }
                    is Result.Error -> {
                        _error.value = result.exception.message
                        _loadingState.value = false
                    }
                    is Result.Loading -> {
                        _loadingState.value = true
                    }
                }
            }
        }
    }
}
