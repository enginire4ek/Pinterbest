package com.example.pinterbest.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinterbest.data.models.User
import com.example.pinterbest.data.repository.Repository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response

class RegistrationViewModel(userData: User, repository: Repository) :
    ViewModel() {

    private val _signInCodeLiveData = MutableLiveData<Response<ResponseBody>>()
    val signInCodeLiveData: LiveData<Response<ResponseBody>>
        get() = _signInCodeLiveData

    init {
        viewModelScope.launch {
            _signInCodeLiveData.value = repository.postsignUp(user = userData)
        }
    }
}
