package com.example.pinterbest.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinterbest.domain.common.Result
import com.example.pinterbest.domain.entities.Profile
import com.example.pinterbest.domain.usecases.GetPinsCreatorsUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreatorsViewModel @Inject constructor(
    private val getPinsCreatorsUseCase: GetPinsCreatorsUseCase
) : ViewModel() {
    private val _loadingState = MutableLiveData(false)
    val loadingState: LiveData<Boolean> = _loadingState

    private val _creators = SingleLiveEvent<List<Profile>?>()
    val creators: SingleLiveEvent<List<Profile>?> = _creators

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getCreators() {
        viewModelScope.launch {
            getPinsCreatorsUseCase().collect { result ->
                when (result) {
                    is Result.Success -> {
                        _creators.value = result.data
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
