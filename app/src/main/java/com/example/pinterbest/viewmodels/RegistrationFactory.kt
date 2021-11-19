package com.example.pinterbest.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pinterbest.data.repository.Repository

class RegistrationFactory(application: Application, val repository: Repository) :
    ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegistrationViewModel(repository) as T
    }
}
