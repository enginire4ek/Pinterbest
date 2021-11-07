package com.example.pinterbest.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.example.pinterbest.data.repository.Repository

class HomeViewModel(application: Application, repository: Repository) :
    AndroidViewModel(application) {
    val pinsFeedLiveData = repository.getpinFeed().asLiveData()
}
