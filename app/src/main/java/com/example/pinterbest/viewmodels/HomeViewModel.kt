package com.example.pinterbest.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.pinterbest.data.repository.Repository

class HomeViewModel(context: Context, repository: Repository) :
    ViewModel() {
    val pinsFeedLiveData = repository.getpinFeed(context).asLiveData()
}
