package com.example.pinterbest.presentation.viewmodels

import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinterbest.domain.common.ResponseWithCookie
import com.example.pinterbest.domain.common.Result
import com.example.pinterbest.domain.entities.UserSignUp
import com.example.pinterbest.domain.usecases.PostSingUpUseCase
import com.example.pinterbest.presentation.utilities.ErrorMessageGenerator
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SignUpViewModel @Inject constructor(
    private val postSingUpUseCase: PostSingUpUseCase
) : ViewModel() {

    val response = SingleLiveEvent<ResponseWithCookie?>()

    private val _error = MutableLiveData<Int>()
    val error: LiveData<Int> = _error

    fun postSigUp(username: EditText, email: EditText, password: EditText) {
        viewModelScope.launch {
            postSingUpUseCase(
                UserSignUp(
                    username.text.toString(),
                    email.text.toString(),
                    password.text.toString()
                )
            ).collect { result ->
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
