package com.example.pinterbest.api

import com.example.pinterbest.data.models.PinsFeed
import com.example.pinterbest.data.models.Profile
import com.example.pinterbest.data.models.User
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @GET("pins/feed?offset=0&amount=20")
    suspend fun getPinFeed(@Header("Cookie") cookie: String = ""): Response<PinsFeed>

    @POST("auth/signup")
    suspend fun postSignUp(@Body userData: User): Response<ResponseBody>

    @GET("profile")
    suspend fun getProfile(@Header("Cookie") cookie: String = ""): Response<Profile>
}
