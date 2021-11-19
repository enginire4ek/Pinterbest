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
                INVALID_DATA -> throwInvalidDataException()
                WRONG_PASSWORD -> throwWrongPasswordException()
                ALREADY_AUTHORIZED -> throwAlreadyAuthorizedException()
                USER_NOT_FOUND -> throwUserNotFoundException()
                USER_EXISTS -> throwUserExistsException()
                else -> throw IllegalStateException()
            }
        }
    }

    private fun throwInvalidDataException() {
        throw InvalidDataException()
    }

    private fun throwWrongPasswordException() {
        throw WrongPasswordException()
    }

    private fun throwAlreadyAuthorizedException() {
        throw AlreadyAuthorizedException()
    }

    private fun throwUserNotFoundException() {
        throw UserNotFoundException()
    }

    private fun throwUserExistsException() {
        throw UserExistsException()
    }

    companion object {
        const val INVALID_DATA = 400
        const val WRONG_PASSWORD = 401
        const val ALREADY_AUTHORIZED = 403
        const val USER_NOT_FOUND = 404
        const val USER_EXISTS = 409
    }
}
