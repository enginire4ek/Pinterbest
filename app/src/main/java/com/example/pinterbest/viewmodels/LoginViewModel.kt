package com.example.pinterbest.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun setLiveEvent(userData: UserLogin) {
        viewModelScope.launch {
            logInCodeLiveData.value = repository.postlogIn(user = userData)
        }
    }
}
