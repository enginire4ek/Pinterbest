package com.example.pinterbest.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinterbest.domain.common.Result
import com.example.pinterbest.domain.entities.BoardsList
import com.example.pinterbest.domain.entities.Profile
import com.example.pinterbest.domain.usecases.GetCheckAuthUseCase
import com.example.pinterbest.domain.usecases.GetProfileBoardsUseCase
import com.example.pinterbest.domain.usecases.GetProfileDetailsUseCase
import com.example.pinterbest.domain.usecases.SaveSessionToPrefsUseCase
import com.example.pinterbest.presentation.R
import com.example.pinterbest.presentation.utilities.ErrorMessageGenerator
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProfileViewModel @Inject constructor(
    private val getProfileDetailsUseCase: GetProfileDetailsUseCase,
    private val getProfileBoardsUseCase: GetProfileBoardsUseCase,
    private val saveSessionToPrefsUseCase: SaveSessionToPrefsUseCase,
    private val getCheckAuthUseCase: GetCheckAuthUseCase,
) : ViewModel() {

    private val _state = MutableLiveData(true)
    val state: LiveData<Boolean> = _state

    private val _profile = MutableLiveData<Profile?>()
    val profile: LiveData<Profile?> = _profile

    private val _boards = SingleLiveEvent<BoardsList?>()
    val boards: LiveData<BoardsList?> = _boards

    private val _error = SingleLiveEvent<Int?>()
    val error: LiveData<Int?> = _error

    private val _loggedIn = SingleLiveEvent<Boolean>()
    val loggedIn: LiveData<Boolean> = _loggedIn

    private val _checkAuthState = SingleLiveEvent<Boolean>()
    val checkAuthState: LiveData<Boolean> = _checkAuthState

    private val _checkAuthError = SingleLiveEvent<Int?>()
    val checkAuthError: LiveData<Int?> = _checkAuthError

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

    fun getProfileBoards(id: Int) {
        viewModelScope.launch {
            getProfileBoardsUseCase(id.toString()).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _boards.value = result.data
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

    fun findSavedPinsBoardId(boardsList: BoardsList): Int? {
        var id: Int? = null
        boardsList.boards.forEach {
            if (it.title == "Saved pins") {
                id = it.ID
            }
        }
        return id
    }

    fun saveCookie() {
        saveSessionToPrefsUseCase("")
    }
}
