package com.example.pinterbest.viewmodels

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinterbest.R
import com.example.pinterbest.data.exceptions.AlreadyAuthorizedException
import com.example.pinterbest.data.exceptions.InvalidDataException
import com.example.pinterbest.data.exceptions.UserNotFoundException
import com.example.pinterbest.data.exceptions.WrongPasswordException
import com.example.pinterbest.data.models.UserLogin
import com.example.pinterbest.data.repository.Repository
import com.example.pinterbest.utilities.ResourceProvider
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

    fun <T> processErrorCode(resources: Resources, t: T): String {
        return when (t) {
            is AlreadyAuthorizedException -> {
                ResourceProvider(resources).getString(R.string.error_already_authorized)
            }
            is InvalidDataException -> {
                ResourceProvider(resources).getString(R.string.error_invalid_data)
            }
            is WrongPasswordException -> {
                ResourceProvider(resources).getString(R.string.error_wrong_password)
            }
            is UserNotFoundException -> {
                ResourceProvider(resources).getString(R.string.error_user_not_found)
            }
            else -> {
                ResourceProvider(resources).getString(R.string.apology_text)
            }
        }
    }

    fun mapToUserLogin(username: String, password: String) = UserLogin(username, password)
}
