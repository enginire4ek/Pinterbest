package com.example.pinterbest.data.api

import com.example.pinterbest.data.BuildConfig
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    private var mInstance: ApiClient? = null
    private val mRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient().build())
        .build()

    fun getInstance(): ApiClient {
        if (mInstance == null) {
            synchronized(this) {
                if (mInstance == null) {
                    mInstance = ApiClient()
                }
            }
        }
        return mInstance!!
    }

    fun getClient(): ApiService {
        return mRetrofit.create(ApiService::class.java)
    }

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

    companion object {
        const val BASE_URL = "https://pinterbest.ru/api/"
    }
}
