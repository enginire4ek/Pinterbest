package com.example.pinterbest.data.repository

import com.example.pinterbest.data.api.ApiClient
import com.example.pinterbest.data.common.ErrorMessage
import com.example.pinterbest.data.models.toPin
import com.example.pinterbest.domain.common.Result
import com.example.pinterbest.domain.entities.Pin
import com.example.pinterbest.domain.entities.PinsList
import com.example.pinterbest.domain.repositories.PinsRepository
import com.example.pinterbest.domain.repositories.SessionRepository
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

class PinsRepositoryImpl
@Inject constructor(private val sessionRepository: SessionRepository) : PinsRepository {
    override suspend fun getPins(): Flow<Result<PinsList>> = flow {
        try {
            emit(Result.Loading)
            val pins = ApiClient().getInstance().getClient().getPinFeed(
                sessionRepository.authProvider() ?: ""
            )
            emit(Result.Success(pins))
        } catch (e: HttpException) {
            if (ErrorMessage.ErrorMap[e.code()] != null) {
                emit(Result.Error(ErrorMessage.ErrorMap[e.code()]!!))
            } else {
                emit(Result.Error(IOException()))
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getPinDetails(pinId: Int): Flow<Result<Pin>> = flow {
        try {
            emit(Result.Loading)
            val pins = ApiClient().getInstance().getClient().getPinById(
                pinId.toString()
            ).toPin()
            emit(Result.Success(pins))
        } catch (e: HttpException) {
            if (ErrorMessage.ErrorMap[e.code()] != null) {
                emit(Result.Error(ErrorMessage.ErrorMap[e.code()]!!))
            } else {
                emit(Result.Error(IOException()))
            }
        }
    }.flowOn(Dispatchers.IO)
}
