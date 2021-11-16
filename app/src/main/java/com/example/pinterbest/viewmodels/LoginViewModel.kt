package com.example.pinterbest.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinterbest.data.models.UserLogin
import com.example.pinterbest.data.repository.Repository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response

class LoginViewModel(userData: UserLogin, repository: Repository) :
    ViewModel() {

    private val _logInCodeLiveData = MutableLiveData<Response<ResponseBody>>()
    val logInCodeLiveData: LiveData<Response<ResponseBody>>
        get() = _logInCodeLiveData

    init {
        viewModelScope.launch {
            _logInCodeLiveData.value = repository.postlogIn(user = userData)
        }
    }
}
