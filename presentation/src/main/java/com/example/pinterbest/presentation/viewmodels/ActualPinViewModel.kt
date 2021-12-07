package com.example.pinterbest.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinterbest.domain.common.Result
import com.example.pinterbest.domain.entities.Profile
import com.example.pinterbest.domain.usecases.GetProfileDetailsByIdUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class ActualPinViewModel @Inject constructor(
    private val getProfileDetailsByIdUseCase: GetProfileDetailsByIdUseCase
) : ViewModel() {
    private val _state = MutableLiveData(true)
    val state: LiveData<Boolean> = _state

    private val _profile = SingleLiveEvent<Profile?>()
    val profile: SingleLiveEvent<Profile?> = _profile

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getProfileDetailsById(userId:Int) {
        viewModelScope.launch {
            getProfileDetailsByIdUseCase(userId).collect { result ->
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
    }
}