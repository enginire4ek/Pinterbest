package com.example.pinterbest.di

import android.content.SharedPreferences
import com.example.pinterbest.data.repository.AuthRepositoryImpl
import com.example.pinterbest.data.repository.SessionRepositoryImpl
import com.example.pinterbest.domain.repositories.AuthRepository
import com.example.pinterbest.domain.repositories.SessionRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository(sessionRepository: SessionRepository): AuthRepository {
        return AuthRepositoryImpl(sessionRepository)
    }

    @Provides
    @Singleton
    fun provideSessionRepository(preferences: SharedPreferences): SessionRepository {
        return SessionRepositoryImpl(preferences)
    }
}
