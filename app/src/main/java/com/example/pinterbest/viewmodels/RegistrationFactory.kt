package com.example.pinterbest.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pinterbest.data.models.User
import com.example.pinterbest.data.repository.Repository

class RegistrationFactory(application: Application, val user: User, val repository: Repository) :
    ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegistrationViewModel(user, repository) as T
    }
}
