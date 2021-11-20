package com.example.pinterbest.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.pinterbest.data.repository.Repository

class ProfileViewModel(repository: Repository) :
    ViewModel() {
    val profileLiveData = repository.getProfile().asLiveData()
}
