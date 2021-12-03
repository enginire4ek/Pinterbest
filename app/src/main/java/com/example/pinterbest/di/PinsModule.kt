package com.example.pinterbest.di

import android.content.SharedPreferences
import com.example.pinterbest.data.repository.PinsRepositoryImpl
import com.example.pinterbest.domain.repositories.PinsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object PinsModule {

    @Provides
    @Singleton
    fun providePinsRepository(preferences: SharedPreferences): PinsRepository {
        return PinsRepositoryImpl(preferences)
    }
}
