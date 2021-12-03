package com.example.pinterbest.di

import android.content.SharedPreferences
import com.example.pinterbest.data.repository.ProfileRepositoryImpl
import com.example.pinterbest.domain.repositories.ProfileRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object ProfileModule {

    @Provides
    @Singleton
    fun provideProfileRepository(preferences: SharedPreferences): ProfileRepository {
        return ProfileRepositoryImpl(preferences)
    }
}
