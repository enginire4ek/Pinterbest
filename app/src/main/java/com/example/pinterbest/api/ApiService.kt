package com.example.pinterbest.api

import com.example.pinterbest.data.models.PinsFeed
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("pins/feed?offset=0&amount=20")
    suspend fun getPinFeed(): Response<PinsFeed>
}
