package com.example.pinterbest.di

import android.content.Context
import com.example.pinterbest.presentation.common.ApplicationAPI
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class, PinsModule::class,
        ViewModelModule::class, AuthModule::class, ProfileModule::class, SessionModule::class
    ]
)
interface ApplicationComponent : ApplicationAPI {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}
