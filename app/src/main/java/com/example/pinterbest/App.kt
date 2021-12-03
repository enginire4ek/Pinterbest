package com.example.pinterbest

import android.app.Application
import com.example.pinterbest.di.ApplicationComponent
import com.example.pinterbest.di.DaggerApplicationComponent
import com.example.pinterbest.presentation.common.ApplicationAPI
import com.example.pinterbest.presentation.common.ApplicationComponentHolder

class App : Application(), ApplicationComponentHolder {

    private lateinit var appComponent: ApplicationComponent

    override fun getApplicationAPI(): ApplicationAPI {
        return appComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent
            .factory()
            .create(applicationContext)
    }
}
