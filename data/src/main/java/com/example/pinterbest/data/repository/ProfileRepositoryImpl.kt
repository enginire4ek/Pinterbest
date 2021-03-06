package com.example.pinterbest.data.repository

import com.example.pinterbest.data.api.ApiService
import com.example.pinterbest.data.common.ErrorMessage
import com.example.pinterbest.data.models.toProfile
import com.example.pinterbest.domain.common.Result
import com.example.pinterbest.domain.entities.Profile
import com.example.pinterbest.domain.repositories.PinsRepository
import com.example.pinterbest.domain.repositories.ProfileRepository
import com.example.pinterbest.domain.repositories.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

class ProfileRepositoryImpl
@Inject constructor(
    private val pinsRepository: PinsRepository,
    private val sessionRepository: SessionRepository,
    private val authClient: ApiService
) : ProfileRepository {
    override suspend fun getPinsCreators(set: Set<Int>): Flow<Result<List<Profile>>> = flow {
        emit(Result.Loading)
        try {
            val profiles = mutableListOf<Profile>()
            pinsRepository.getCreators().forEach {
                profiles.add(
                    authClient
                        .getProfileById(it.toString())
                        .toProfile()
                )
            }
            emit(Result.Success(profiles))
        } catch (e: HttpException) {
            if (ErrorMessage.ErrorMap[e.code()] != null) {
                emit(Result.Error(e))
            }
        } catch (e: UnknownHostException) {
            emit(Result.Error(UnknownHostException()))
        }
    }

    override suspend fun getProfileDetails(): Flow<Result<Profile>> = flow {
        emit(Result.Loading)
        try {
            val profile = authClient
                .getProfile(sessionRepository.authProvider() ?: "")
                .toProfile()
            emit(Result.Success(profile))
        } catch (e: HttpException) {
            if (ErrorMessage.ErrorMap[e.code()] != null) {
                emit(Result.Error(e))
            }
        } catch (e: UnknownHostException) {
            emit(Result.Error(UnknownHostException()))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getProfileDetailsById(userId: Int): Flow<Result<Profile>> = flow {
        emit(Result.Loading)
        try {
            val profile = authClient
                .getProfileById(userId.toString())
                .toProfile()
            emit(Result.Success(profile))
        } catch (e: HttpException) {
            if (ErrorMessage.ErrorMap[e.code()] != null) {
                emit(Result.Error(e))
            }
        } catch (e: UnknownHostException) {
            emit(Result.Error(e))
        }
    }.flowOn(Dispatchers.IO)
}
