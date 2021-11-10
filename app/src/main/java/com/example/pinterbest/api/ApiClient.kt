package com.example.pinterbest.api

import com.example.pinterbest.BuildConfig
import com.example.pinterbest.api.ApiEndpoints.BASE_URL
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    private fun okHttpClient(): OkHttpClient.Builder {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.connectTimeout(2, TimeUnit.MINUTES)
        okHttpClient.readTimeout(2, TimeUnit.MINUTES)
        okHttpClient.writeTimeout(2, TimeUnit.MINUTES)

        when {
            BuildConfig.DEBUG -> {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                okHttpClient.addInterceptor(logging)
            }
        }

        return okHttpClient
    }

    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit {
        val okhttpBuilder = okHttpClient()
        when (retrofit) {
            null ->
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okhttpBuilder.build())
                    .build()
        }
        return retrofit as Retrofit
    }
}
