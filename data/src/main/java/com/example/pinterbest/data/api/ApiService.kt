package com.example.pinterbest.data.api

import com.example.pinterbest.data.models.DataPinObject
import com.example.pinterbest.data.models.DataProfile
import com.example.pinterbest.domain.entities.PinsList
import com.example.pinterbest.domain.entities.UserLogIn
import com.example.pinterbest.domain.entities.UserSignUp
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("pins/feed?offset=0&amount=20")
    suspend fun getPinFeed(@Header("Cookie") cookie: String = ""): PinsList

    @GET("pins/{id}")
    suspend fun getPinById(@Path("id") id: String): DataPinObject

    @POST("auth/signup")
    suspend fun postSignUp(@Body userData: UserSignUp): Response<ResponseBody>

    @POST("auth/login")
    suspend fun postLogIn(@Body userData: UserLogIn): Response<ResponseBody>

    @GET("auth/check")
    suspend fun getAuthCheck(@Header("Cookie") cookie: String = ""): Response<ResponseBody>

    @GET("profile")
    suspend fun getProfile(@Header("Cookie") cookie: String = ""): DataProfile
}
