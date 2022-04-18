package com.example.pinterbest.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinterbest.domain.common.Result
import com.example.pinterbest.domain.entities.Profile
import com.example.pinterbest.domain.repositories.PinsRepository
import com.example.pinterbest.domain.repositories.ProfileRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreatorsViewModel @Inject constructor(
    private val pinsRepository: PinsRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _state = MutableLiveData(false)
    val state: LiveData<Boolean> = _state

    private val _creators = SingleLiveEvent<List<Profile>?>()
    val creators: SingleLiveEvent<List<Profile>?> = _creators

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getCreators() {
        viewModelScope.launch {
            profileRepository.getPinsCreators(
                pinsRepository.getCreators()
            ).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _creators.value = result.data
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
