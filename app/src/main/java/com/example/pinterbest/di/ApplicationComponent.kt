package com.example.pinterbest.di

import android.content.Context
import com.example.pinterbest.presentation.common.ApplicationApi
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        BusinessModule::class,
        ViewModelModule::class,
        AuthModule::class
    ]
)
interface ApplicationComponent : ApplicationApi {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}
