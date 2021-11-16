package com.example.pinterbest.data.repository

import java.lang.IllegalStateException
import okhttp3.ResponseBody
import retrofit2.Response

class AuthRepository(private val sessionRepository: SessionRepository) {

    fun saveSession(response: Response<ResponseBody>) {
        val cookie = response.headers().values("Set-Cookie")[0].substringBefore(";")
        sessionRepository.saveUserData(cookie)
    }

    @Throws(IllegalStateException::class)
    fun authProvider(response: Response<ResponseBody>) {
        return if (response.isSuccessful) {
            saveSession(response)
        } else {
            throw IllegalStateException("${response.code()}")
        }
    }
}
