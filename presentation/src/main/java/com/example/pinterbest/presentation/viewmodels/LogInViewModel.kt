package com.example.pinterbest.presentation.viewmodels

import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinterbest.domain.common.ResponseWithCookie
import com.example.pinterbest.domain.common.Result
import com.example.pinterbest.domain.entities.UserLogIn
import com.example.pinterbest.domain.usecases.PostLogInUseCase
import com.example.pinterbest.presentation.utilities.ErrorMessageGenerator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class LogInViewModel @Inject constructor(
    private val postLogInUseCase: PostLogInUseCase
) : ViewModel() {

    val response = SingleLiveEvent<ResponseWithCookie?>()

    private val _error = MutableLiveData<Int>()
    val error: LiveData<Int> = _error

    fun postLogIn(username: EditText, password: EditText) {
        viewModelScope.launch {
            postLogInUseCase(UserLogIn(username.text.toString(), password.text.toString()))
                .collect { result ->
                    when (result) {
                        is Result.Success -> {
                            response.value = result.data
                        }
                        is Result.Error -> {
                            _error.value = ErrorMessageGenerator.processErrorCode(result.exception)
                        }
                        else -> {}
                    }
                }
        }
    }
}
