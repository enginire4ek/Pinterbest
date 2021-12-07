package com.example.pinterbest.data.repository

import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import com.example.pinterbest.data.api.ApiClient
import com.example.pinterbest.data.common.ErrorMessage
import com.example.pinterbest.data.common.UploadRequestBody
import com.example.pinterbest.data.models.*
import com.example.pinterbest.domain.common.Result
import com.example.pinterbest.domain.entities.*
import com.example.pinterbest.domain.repositories.PinsRepository
import com.example.pinterbest.domain.repositories.SessionRepository
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.NullPointerException
import java.net.UnknownHostException

class PinsRepositoryImpl
@Inject constructor(private val sessionRepository: SessionRepository) : PinsRepository {
    override suspend fun getPins(): Flow<Result<PinsList>> = flow {
        try {
            emit(Result.Loading)
            val pins = ApiClient().getInstance().getClient().getPinFeed(
                sessionRepository.authProvider() ?: ""
            ).toPinsList()
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
                emit(Result.Error(e))
            } else {
                emit(Result.Error(IOException()))
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun postPin(pinInfo: PinInfo, byteArray: ByteArray): Flow<Result<IdEntity>> = flow {
        try {
            emit(Result.Loading)

            val bodyImage = MultipartBody.Part.createFormData(
                "photo[content]", "photo",
                byteArray.toRequestBody("multipart/form-data".toMediaTypeOrNull(), 0, byteArray.size)
            )

            val pinResponse = ApiClient().getInstance().getClient()
                .postPin(pinInfo, bodyImage, sessionRepository.authProvider() ?: "")
                .toIdEntity()
            emit(Result.Success(pinResponse))
        } catch (e: HttpException) {
            if (ErrorMessage.ErrorMap[e.code()] != null) {
                emit(Result.Error(e))
            } else {
                emit(Result.Error(UnknownHostException()))
            }
        } catch (e: NullPointerException) {
            emit(Result.Error(e))
        }
    }.flowOn(Dispatchers.IO)
}
