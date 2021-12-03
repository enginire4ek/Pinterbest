package com.example.pinterbest.data.repository

import android.content.SharedPreferences
import com.example.pinterbest.data.api.ApiClient
import com.example.pinterbest.data.models.toProfile
import com.example.pinterbest.domain.common.ErrorMessage
import com.example.pinterbest.domain.common.Result
import com.example.pinterbest.domain.entities.Profile
import com.example.pinterbest.domain.repositories.ProfileRepository
import java.net.UnknownHostException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

class ProfileRepositoryImpl(val preferences: SharedPreferences) : ProfileRepository {
    override suspend fun getProfileDetails(): Flow<Result<Profile>> = flow {
        emit(Result.Loading)
        try {
            val profile = ApiClient().getInstance().getClient().getProfile(
                SessionRepositoryImpl(preferences).authProvider() ?: ""
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
