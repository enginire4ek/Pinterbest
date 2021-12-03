package com.example.pinterbest.presentation.common

import android.content.Context
import androidx.fragment.app.Fragment

val Context.appComponent: ApplicationAPI
    get() = when (this) {
        is ApplicationComponentHolder -> getApplicationAPI()
        else -> this.applicationContext.appComponent
    }

fun Fragment.getAppComponent() = requireContext().appComponent
