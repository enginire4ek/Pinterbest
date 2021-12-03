package com.example.pinterbest.data.repository

import android.content.SharedPreferences
import com.example.pinterbest.data.api.ApiClient
import com.example.pinterbest.domain.common.ErrorMessage
import com.example.pinterbest.domain.common.ResponseWithCookie
import com.example.pinterbest.domain.common.Result
import com.example.pinterbest.domain.entities.UserLogIn
import com.example.pinterbest.domain.entities.UserSignUp
import com.example.pinterbest.domain.repositories.AuthRepository
import java.net.UnknownHostException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthRepositoryImpl(val preferences: SharedPreferences) : AuthRepository {
    override suspend fun getCheckAuth(): Flow<Result<Int>> = flow {
        emit(Result.Loading)
        try {
            val response = ApiClient().getInstance().getClient().getAuthCheck(
                SessionRepositoryImpl(preferences).authProvider() ?: ""
            )
            if (response.isSuccessful) {
                emit(Result.Success(response.code()))
            } else {
                if (ErrorMessage.ErrorMap[response.code()] != null) {
                    emit(Result.Error(ErrorMessage.ErrorMap[response.code()]!!))
                }
            }
        } catch (e: UnknownHostException) {
            emit(Result.Error(UnknownHostException()))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun postLogIn(userLogIn: UserLogIn):
        Flow<Result<ResponseWithCookie>> = flow {
            emit(Result.Loading)
            try {
                val response = ApiClient().getInstance().getClient().postLogIn(
                    userData = userLogIn
                )
                if (response.isSuccessful) {
                    val responseWithCookie = ResponseWithCookie(
                        response.code(),
                        response.headers().values("Set-Cookie")[0].substringBefore(";")
                    )
                    emit(Result.Success(responseWithCookie))
                    saveCookieToPreferences(responseWithCookie.cookie)
                } else {
                    if (ErrorMessage.ErrorMap[response.code()] != null) {
                        emit(Result.Error(ErrorMessage.ErrorMap[response.code()]!!))
                    }
                }
            } catch (e: UnknownHostException) {
                emit(Result.Error(UnknownHostException()))
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun postSignUp(userSignUp: UserSignUp):
        Flow<Result<ResponseWithCookie>> = flow {
            emit(Result.Loading)
            try {
                val response = ApiClient().getInstance().getClient().postSignUp(
                    userData = userSignUp
                )
                if (response.isSuccessful) {
                    val responseWithCookie = ResponseWithCookie(
                        response.code(),
                        response.headers().values("Set-Cookie")[0].substringBefore(";")
                    )
                    emit(Result.Success(responseWithCookie))
                    saveCookieToPreferences(responseWithCookie.cookie)
                } else {
                    if (ErrorMessage.ErrorMap[response.code()] != null) {
                        emit(Result.Error(ErrorMessage.ErrorMap[response.code()]!!))
                    }
                }
            } catch (e: UnknownHostException) {
                emit(Result.Error(UnknownHostException()))
            }
        }.flowOn(Dispatchers.IO)

    override fun saveCookieToPreferences(cookie: String) {
        SessionRepositoryImpl(preferences).saveSession(cookie)
    }
}
