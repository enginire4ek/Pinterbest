package com.example.pinterbest.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinterbest.domain.common.Result
import com.example.pinterbest.domain.entities.BoardsList
import com.example.pinterbest.domain.entities.Profile
import com.example.pinterbest.domain.usecases.GetProfileBoardsUseCase
import com.example.pinterbest.domain.usecases.GetProfileDetailsUseCase
import com.example.pinterbest.domain.usecases.SaveSessionToPrefsUseCase
import com.example.pinterbest.presentation.utilities.ErrorMessageGenerator
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProfileViewModel @Inject constructor(
    private val getProfileDetailsUseCase: GetProfileDetailsUseCase,
    private val getProfileBoardsUseCase: GetProfileBoardsUseCase,
    private val saveSessionToPrefsUseCase: SaveSessionToPrefsUseCase
) : ViewModel() {

    private val _state = MutableLiveData(true)
    val state: LiveData<Boolean> = _state

    private val _profile = MutableLiveData<Profile?>()
    val profile: LiveData<Profile?> = _profile

    private val _boards = SingleLiveEvent<BoardsList?>()
    val boards: SingleLiveEvent<BoardsList?> = _boards

    private val _error = MutableLiveData<Int>()
    val error: LiveData<Int> = _error

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

    fun getProfileBoards(id: Int) {
        viewModelScope.launch {
            getProfileBoardsUseCase(id.toString()).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _boards.value = result.data
                        _state.value = false
                    }
                    /*is Result.Error -> {
                        _error.value = result.exception.message
                        _state.value = false
                    }*/
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
