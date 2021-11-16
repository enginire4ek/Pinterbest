package com.example.pinterbest.data.repository

import okhttp3.ResponseBody
import retrofit2.Response

class AuthRepository(val response: Response<ResponseBody>) {

    fun saveSession(sessionRepository: SessionRepository) {
        val cookie = response.headers().values("Set-Cookie")[0].substringBefore(";")
        sessionRepository.saveUserData(cookie)
    }

    fun authProvider(sessionRepository: SessionRepository): Boolean {
        return if (response.isSuccessful) {
            saveSession(sessionRepository)
            true
        } else false
    }
}
