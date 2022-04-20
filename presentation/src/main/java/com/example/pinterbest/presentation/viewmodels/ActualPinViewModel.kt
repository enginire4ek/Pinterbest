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
    private val _loadingState = MutableLiveData(true)
    val loadingState: LiveData<Boolean> = _loadingState

    private val _profile = SingleLiveEvent<Profile?>()
    val profile: SingleLiveEvent<Profile?> = _profile

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getProfileDetailsById(userId: Int) {
        viewModelScope.launch {
            getProfileDetailsByIdUseCase(userId).collect { result ->
                if (result is Result.Success) {
                    _profile.value = result.data
                    _loadingState.value = false
                } else if (result is Result.Error) {
                    _error.value = result.exception.message
                    _loadingState.value = false
                }
            }
        }
    }
}
