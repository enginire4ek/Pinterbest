package com.example.pinterbest.di

import com.example.pinterbest.data.repository.PinsRepositoryImpl
import com.example.pinterbest.data.repository.ProfileRepositoryImpl
import com.example.pinterbest.domain.repositories.PinsRepository
import com.example.pinterbest.domain.repositories.ProfileRepository
import com.example.pinterbest.domain.repositories.SessionRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object BusinessModule {

    @Provides
    @Singleton
    fun providePinsRepository(sessionRepository: SessionRepository): PinsRepository {
        return PinsRepositoryImpl(sessionRepository)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(sessionRepository: SessionRepository): ProfileRepository {
        return ProfileRepositoryImpl(sessionRepository)
    }
}
