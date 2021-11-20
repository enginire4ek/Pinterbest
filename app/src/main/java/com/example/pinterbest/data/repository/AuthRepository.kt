package com.example.pinterbest.data.repository

import com.example.pinterbest.data.exceptions.AlreadyAuthorizedException
import com.example.pinterbest.data.exceptions.InvalidDataException
import com.example.pinterbest.data.exceptions.UserExistsException
import com.example.pinterbest.data.exceptions.UserNotFoundException
import com.example.pinterbest.data.exceptions.WrongPasswordException
import java.lang.IllegalStateException
import okhttp3.ResponseBody
import retrofit2.Response

class AuthRepository(private val sessionRepository: SessionRepository) {

    fun saveSession(response: Response<ResponseBody>) {
        val cookie = response.headers().values("Set-Cookie")[0].substringBefore(";")
        sessionRepository.saveUserData(cookie)
    }

    fun authProvider(response: Response<ResponseBody>) {
        return if (response.isSuccessful) {
            saveSession(response)
        } else {
            when (response.code()) {
                INVALID_DATA -> throw InvalidDataException()
                WRONG_PASSWORD -> throw WrongPasswordException()
                ALREADY_AUTHORIZED -> throw AlreadyAuthorizedException()
                USER_NOT_FOUND -> throw UserNotFoundException()
                USER_EXISTS -> throw UserExistsException()
                else -> throw IllegalStateException()
            }
        }
    }

    companion object {
        const val INVALID_DATA = 400
        const val WRONG_PASSWORD = 401
        const val ALREADY_AUTHORIZED = 403
        const val USER_NOT_FOUND = 404
        const val USER_EXISTS = 409
    }
}
