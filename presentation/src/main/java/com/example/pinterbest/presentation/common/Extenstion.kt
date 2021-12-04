package com.example.pinterbest.presentation.common

import android.content.Context
import androidx.fragment.app.Fragment

val Context.appComponent: ApplicationApi
    get() = when (this) {
        is ApplicationComponentHolder -> getApplicationApi()
        else -> this.applicationContext.appComponent
    }

fun Fragment.getAppComponent() = requireContext().appComponent
