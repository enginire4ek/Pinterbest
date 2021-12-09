package com.example.pinterbest.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinterbest.domain.common.Result
import com.example.pinterbest.domain.common.WrongPasswordException
import com.example.pinterbest.domain.entities.Profile
import com.example.pinterbest.domain.usecases.GetCheckAuthUseCase
import com.example.pinterbest.domain.usecases.GetProfileDetailsUseCase
import com.example.pinterbest.domain.usecases.SaveSessionToPrefsUseCase
import com.example.pinterbest.presentation.R
import com.example.pinterbest.presentation.utilities.ErrorMessageGenerator
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProfileViewModel @Inject constructor(
    private val getProfileDetailsUseCase: GetProfileDetailsUseCase,
    private val saveSessionToPrefsUseCase: SaveSessionToPrefsUseCase,
    private val getCheckAuthUseCase: GetCheckAuthUseCase
) : ViewModel() {

    private val _state = MutableLiveData(true)
    val state: LiveData<Boolean> = _state

    private val _profile = MutableLiveData<Profile?>()
    val profile: LiveData<Profile?> = _profile

    private val _error = MutableLiveData<Int>()
    val error: LiveData<Int> = _error

    private val _loggedIn = MutableLiveData<Boolean>()
    val loggedIn: LiveData<Boolean> = _loggedIn

    private val _checkAuthState = MutableLiveData<Boolean>()
    val checkAuthState: LiveData<Boolean> = _checkAuthState

    private val _checkAuthError = MutableLiveData<Int>()
    val checkAuthError: LiveData<Int> = _checkAuthError

    fun getProfileDetails() {
        viewModelScope.launch {
            getProfileDetailsUseCase().collect { result ->
                when (result) {
                    is Result.Success -> {
                        _profile.value = result.data
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

    fun getAuthStatus() {
        viewModelScope.launch {
            getCheckAuthUseCase().collect { result ->
                when (result) {
                    is Result.Success -> {
                        _loggedIn.value = true
                        _checkAuthState.value = false
                    }
                    is Result.Error -> {
                        val error = ErrorMessageGenerator.processErrorCode(result.exception)
                        if (error == R.string.error_wrong_password) {
                            _loggedIn.value = false
                        } else {
                            _checkAuthError.value = error
                        }
                        _checkAuthState.value = false
                    }
                    is Result.Loading -> {
                        _checkAuthState.value = true
                    }
                }
            }
        }
    }

    fun saveCookie() {
        saveSessionToPrefsUseCase("")
    }
}
