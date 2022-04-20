package com.example.pinterbest.domain.repositories

import com.example.pinterbest.domain.common.Result
import com.example.pinterbest.domain.entities.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun getProfileDetails(): Flow<Result<Profile>>

    suspend fun getProfileDetailsById(userId: Int): Flow<Result<Profile>>

    suspend fun getPinsCreators(set: Set<Int>): Flow<Result<List<Profile>>>
}
