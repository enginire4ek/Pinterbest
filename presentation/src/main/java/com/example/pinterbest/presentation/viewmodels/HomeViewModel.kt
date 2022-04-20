package com.example.pinterbest.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinterbest.domain.common.Result
import com.example.pinterbest.domain.entities.PinsList
import com.example.pinterbest.domain.usecases.GetPinsByBoardIdUseCase
import com.example.pinterbest.domain.usecases.GetPinsByPageUseCase
import com.example.pinterbest.presentation.utilities.ErrorMessageGenerator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getPinsByPageUseCase: GetPinsByPageUseCase,
    private val getPinsByBoardIdUseCase: GetPinsByBoardIdUseCase
) : ViewModel() {

    private val _loadingState = MutableLiveData(true)
    val loadingState: LiveData<Boolean> = _loadingState

    private val _pins = MutableLiveData<PinsList?>()
    val pins: LiveData<PinsList?> = _pins

    private val _error = MutableLiveData<Int>()
    val error: LiveData<Int> = _error

    fun getPins() {
        viewModelScope.launch {
            getPinsByPageUseCase().collect { result ->
                when (result) {
                    is Result.Success -> {
                        _pins.value = result.data
                        _loadingState.value = false
                    }
                    is Result.Error -> {
                        _error.value = ErrorMessageGenerator.processErrorCode(result.exception)
                        _loadingState.value = false
                    }
                    is Result.Loading -> {
                        _loadingState.value = true
                    }
                }
            }
        }
    }

    fun getPinDetailsById(id: Int) {
        viewModelScope.launch {
            getPinsByBoardIdUseCase(id).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _pins.value = result.data
                        _loadingState.value = false
                    }
                    is Result.Error -> {
                        _error.value = ErrorMessageGenerator.processErrorCode(result.exception)
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
