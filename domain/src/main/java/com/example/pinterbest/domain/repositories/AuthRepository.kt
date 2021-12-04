package com.example.pinterbest.domain.repositories

import com.example.pinterbest.domain.common.ResponseWithCookie
import com.example.pinterbest.domain.common.Result
import com.example.pinterbest.domain.entities.UserLogIn
import com.example.pinterbest.domain.entities.UserSignUp
import java.lang.Exception
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun getCheckAuth(): Flow<Result<Int>>

    suspend fun postLogIn(userLogIn: UserLogIn): Flow<Result<ResponseWithCookie>>

    suspend fun postSignUp(userSignUp: UserSignUp): Flow<Result<ResponseWithCookie>>

    fun saveCookieToPreferences(cookie: String)

    fun checkErrorCodeOnLogin(exception: Exception): Boolean
}
