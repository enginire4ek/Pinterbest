package com.example.pinterbest.data.repository

import com.example.pinterbest.data.api.ApiService
import com.example.pinterbest.data.common.ErrorMessage
import com.example.pinterbest.data.models.toIdEntity
import com.example.pinterbest.data.models.toPinsList
import com.example.pinterbest.domain.common.Result
import com.example.pinterbest.domain.entities.IdEntity
import com.example.pinterbest.domain.entities.PinInfo
import com.example.pinterbest.domain.entities.PinsList
import com.example.pinterbest.domain.repositories.PinsRepository
import com.example.pinterbest.domain.repositories.SessionRepository
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class PinsRepositoryImpl
@Inject constructor(
    private val sessionRepository: SessionRepository,
    private val authClient: ApiService
) : PinsRepository {
    override suspend fun getPins(): Flow<Result<PinsList>> = flow {
        try {
            emit(Result.Loading)
            val pins = authClient.getPinFeed().toPinsList()
            emit(Result.Success(pins))
        } catch (e: HttpException) {
            if (ErrorMessage.ErrorMap[e.code()] != null) {
                emit(Result.Error(ErrorMessage.ErrorMap[e.code()]!!))
            } else {
                emit(Result.Error(IOException()))
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getPinsByBoardId(boardId: Int): Flow<Result<PinsList>> = flow {
        try {
            emit(Result.Loading)
            val pins = authClient.getPinsByBoardId(
                boardId.toString()
            ).toPinsList()
            emit(Result.Success(pins))
        } catch (e: HttpException) {
            if (ErrorMessage.ErrorMap[e.code()] != null) {
                emit(Result.Error(e))
            } else {
                emit(Result.Error(IOException()))
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun postPin(pinInfo: PinInfo, pinImage: ByteArray):
        Flow<Result<IdEntity>> = flow {
            try {
                emit(Result.Loading)

                val bodyImage = MultipartBody.Part.createFormData(
                    "pinImage",
                    "any.jpg",
                    pinImage.toRequestBody("image/*".toMediaTypeOrNull(), 0, pinImage.size)
                )

                val pinResponse = authClient
                    .postPin(pinInfo, bodyImage, sessionRepository.authProvider() ?: "")
                    .toIdEntity()
                emit(Result.Success(pinResponse))
            } catch (e: HttpException) {
                if (ErrorMessage.ErrorMap[e.code()] != null) {
                    emit(Result.Error(e))
                } else {
                    emit(Result.Error(UnknownHostException()))
                }
            }
        }.flowOn(Dispatchers.IO)
}
