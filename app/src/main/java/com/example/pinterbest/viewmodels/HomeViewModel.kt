package com.example.pinterbest.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.pinterbest.data.repository.Repository

class HomeViewModel(repository: Repository) :
    ViewModel() {
    val pinsFeedLiveData = repository.getpinFeed().asLiveData()
}
