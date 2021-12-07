package com.example.pinterbest.data.api

import com.example.pinterbest.data.models.DataIdEntity
import com.example.pinterbest.data.models.DataPinObject
import com.example.pinterbest.data.models.DataPinsList
import com.example.pinterbest.data.models.DataProfile
import com.example.pinterbest.domain.entities.PinInfo
import com.example.pinterbest.domain.entities.UserLogIn
import com.example.pinterbest.domain.entities.UserSignUp
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @GET("pins/feed?offset=0&amount=20")
    suspend fun getPinFeed(@Header("Cookie") cookie: String = ""): DataPinsList

    @GET("pins/{id}")
    suspend fun getPinById(@Path("id") id: String): DataPinObject

    @Multipart
    @POST("pin")
    suspend fun postPin(
        @Part("pinInfo") pinInfo: PinInfo,
        @Part pinImage: MultipartBody.Part,
        @Header("Cookie") cookie: String = ""
    ): DataIdEntity

    @POST("auth/signup")
    suspend fun postSignUp(@Body userData: UserSignUp): Response<ResponseBody>

    @POST("auth/login")
    suspend fun postLogIn(@Body userData: UserLogIn): Response<ResponseBody>

    @GET("auth/check")
    suspend fun getAuthCheck(@Header("Cookie") cookie: String = ""): Response<ResponseBody>

    @GET("profile")
    suspend fun getProfile(@Header("Cookie") cookie: String = ""): DataProfile

    @GET("profile/{id}")
    suspend fun getProfileById(@Path("id") id: String): DataProfile
}
