package com.example.pinterbest.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pinterbest.data.models.UserLogin
import com.example.pinterbest.data.repository.Repository

class LoginFactory(application: Application, val user: UserLogin, val repository: Repository) :
    ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(user, repository) as T
    }
}
