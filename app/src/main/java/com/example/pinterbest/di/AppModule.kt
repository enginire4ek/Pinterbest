package com.example.pinterbest.di

import android.content.Context
import android.content.SharedPreferences
import com.example.pinterbest.data.api.ApiClient
import com.example.pinterbest.data.api.ApiService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AppModule {
    private const val login_info = "login_info"

    @Provides
    @Singleton
    fun providePreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(login_info, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideApiClient(): ApiService {
        return ApiClient().getInstance().getClient()
    }
}
