package com.example.pinterbest.viewmodels

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinterbest.R
import com.example.pinterbest.data.exceptions.AlreadyAuthorizedException
import com.example.pinterbest.data.exceptions.InvalidDataException
import com.example.pinterbest.data.exceptions.UserExistsException
import com.example.pinterbest.data.models.User
import com.example.pinterbest.data.repository.Repository
import com.example.pinterbest.utilities.ResourceProvider
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response

class RegistrationViewModel(val repository: Repository) : ViewModel() {

    val signInCodeLiveData = SingleLiveEvent<Response<ResponseBody>>()

    fun getLiveEvent(): SingleLiveEvent<Response<ResponseBody>> {
        return signInCodeLiveData
    }

    fun setLiveEvent(username: String, email: String, password: String) {
        viewModelScope.launch {
            signInCodeLiveData.value = repository
                .postsignUp(user = mapToUser(username, email, password))
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
            is UserExistsException -> {
                ResourceProvider(resources).getString(R.string.error_user_exists)
            }
            else -> {
                ResourceProvider(resources).getString(R.string.apology_text)
            }
        }
    }

    fun mapToUser(username: String, email: String, password: String) = User(
        username,
        email,
        password
    )
}
