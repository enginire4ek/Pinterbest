package com.example.pinterbest.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinterbest.domain.common.Result
import com.example.pinterbest.domain.entities.PinsList
import com.example.pinterbest.domain.usecases.GetPinsByPageUseCase
import com.example.pinterbest.presentation.utilities.ErrorMessageGenerator
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel @Inject constructor(
    private val getPinsByPageUseCase: GetPinsByPageUseCase
) : ViewModel() {

    private val _state = MutableLiveData(true)
    val state: LiveData<Boolean> = _state

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
                        _state.value = false
                    }
                    is Result.Error -> {
                        _error.value = ErrorMessageGenerator.processErrorCode(result.exception)
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
