package com.example.pinterbest.di

import com.example.pinterbest.data.api.ApiService
import com.example.pinterbest.data.repository.BoardRepositoryImpl
import com.example.pinterbest.data.repository.PinsRepositoryImpl
import com.example.pinterbest.data.repository.ProfileRepositoryImpl
import com.example.pinterbest.domain.repositories.BoardRepository
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
    fun providePinsRepository(
        sessionRepository: SessionRepository,
        authClient: ApiService
    ): PinsRepository {
        return PinsRepositoryImpl(sessionRepository, authClient)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(
        sessionRepository: SessionRepository,
        authClient: ApiService
    ): ProfileRepository {
        return ProfileRepositoryImpl(sessionRepository, authClient)
    }

    @Provides
    @Singleton
    fun provideBoardRepository(
        sessionRepository: SessionRepository,
        authClient: ApiService
    ): BoardRepository {
        return BoardRepositoryImpl(sessionRepository, authClient)
    }
}
