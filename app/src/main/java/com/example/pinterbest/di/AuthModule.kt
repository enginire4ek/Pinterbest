package com.example.pinterbest.di

import android.content.SharedPreferences
import com.example.pinterbest.data.repository.AuthRepositoryImpl
import com.example.pinterbest.domain.repositories.AuthRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository(preferences: SharedPreferences): AuthRepository {
        return AuthRepositoryImpl(preferences)
    }
}
