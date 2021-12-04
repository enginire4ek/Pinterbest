package com.example.pinterbest.presentation.common

import androidx.fragment.app.Fragment
import com.example.pinterbest.presentation.MainActivity
import com.example.pinterbest.presentation.viewmodels.ViewModelFactory

interface ApplicationApi {

    fun inject(activity: MainActivity)

    fun inject(fragment: Fragment)

    fun viewModelsFactory(): ViewModelFactory
}
