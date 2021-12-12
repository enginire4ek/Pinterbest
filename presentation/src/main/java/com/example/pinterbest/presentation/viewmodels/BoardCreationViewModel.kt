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
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BoardCreationViewModel @Inject constructor(
    private val postNewBoardUseCase: PostNewBoardUseCase
) : ViewModel() {
    private val _state = MutableLiveData(false)
    val state: LiveData<Boolean> = _state

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
}
