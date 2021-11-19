package com.example.pinterbest.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinterbest.data.models.User
import com.example.pinterbest.data.repository.Repository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response

class RegistrationViewModel(val repository: Repository) : ViewModel() {

    val signInCodeLiveData = SingleLiveEvent<Response<ResponseBody>>()

    fun getLiveEvent(): SingleLiveEvent<Response<ResponseBody>> {
        return signInCodeLiveData
    }

    fun setLiveEvent(userData: User) {
        viewModelScope.launch {
            signInCodeLiveData.value = repository.postsignUp(user = userData)
        }
    }
}
