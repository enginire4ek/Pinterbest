package com.example.pinterbest.data.repository

import com.example.pinterbest.data.api.ApiClient
import com.example.pinterbest.data.models.toProfile
import com.example.pinterbest.data.common.ErrorMessage
import com.example.pinterbest.domain.common.Result
import com.example.pinterbest.domain.entities.Profile
import com.example.pinterbest.domain.repositories.ProfileRepository
import com.example.pinterbest.domain.repositories.SessionRepository
import java.net.UnknownHostException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject

class ProfileRepositoryImpl
@Inject constructor(private val sessionRepository: SessionRepository): ProfileRepository {
    override suspend fun getProfileDetails(): Flow<Result<Profile>> = flow {
        emit(Result.Loading)
        try {
            val profile = ApiClient().getInstance().getClient().getProfile(
                sessionRepository.authProvider() ?: ""
            ).toProfile()
            emit(Result.Success(profile))
        } catch (e: HttpException) {
            if (ErrorMessage.ErrorMap[e.code()] != null) {
                emit(Result.Error(ErrorMessage.ErrorMap[e.code()]!!))
            }
        } catch (e: UnknownHostException) {
            emit(Result.Error(UnknownHostException()))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getProfileDetailsById(userId: Int): Flow<Result<Profile>> {
        TODO("Not yet implemented")
    }
}
