package com.example.pinterbest.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinterbest.data.exceptions.ErrorMessage
import com.example.pinterbest.data.models.UserLogin
import com.example.pinterbest.data.repository.Repository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response

class LoginViewModel(val repository: Repository) : ViewModel() {

    val logInCodeLiveData = SingleLiveEvent<Response<ResponseBody>>()

    fun getLiveEvent(): SingleLiveEvent<Response<ResponseBody>> {
        return logInCodeLiveData
    }

    fun setLiveEvent(username: String, password: String) {
        viewModelScope.launch {
            logInCodeLiveData.value = repository
                .postlogIn(user = mapToUserLogin(username, password))
        }
    }

    fun <T> getErrorCode(t: T) = ErrorMessage.processErrorCode(t)

    fun mapToUserLogin(username: String, password: String) = UserLogin(username, password)
}
