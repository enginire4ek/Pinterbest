package com.example.pinterbest.data.network

import com.example.pinterbest.data.states.NetworkState
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ApiExecutionRequests {
    const val ResourceForbidden = 403
    const val ResourceNotFound = 404
    const val InternalServerError = 500
    const val BadGateWay = 502
    const val ResourceRemoved = 301
    const val RemovedResourceFound = 302

    private val apiService = ApiClient().getClient().create(ApiService::class.java)

    suspend fun fetchNetworkState(): NetworkState = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getPinFeed()
            if (response.isSuccessful) {
                if (response.body()!!.pins.isNullOrEmpty()) {
                    return@withContext NetworkState.InvalidData
                } else {
                    return@withContext NetworkState.Success(response.body()!!)
                }
            } else {
                when (response.code()) {
                    ResourceForbidden -> {
                        return@withContext NetworkState.HttpErrors.ResourceForbidden(
                            response.message()
                        )
                    }
                    ResourceNotFound -> {
                        return@withContext NetworkState.HttpErrors.ResourceNotFound(
                            response.message()
                        )
                    }
                    InternalServerError -> {
                        return@withContext NetworkState.HttpErrors.InternalServerError(
                            response.message()
                        )
                    }
                    BadGateWay -> {
                        return@withContext NetworkState.HttpErrors.BadGateWay(response.message())
                    }
                    ResourceRemoved -> {
                        return@withContext NetworkState.HttpErrors.ResourceRemoved(
                            response.message()
                        )
                    }
                    RemovedResourceFound -> {
                        return@withContext NetworkState.HttpErrors.RemovedResourceFound(
                            response.message()
                        )
                    }
                    else -> {
                        return@withContext NetworkState.Error(response.message())
                    }
                }
            }
        } catch (error: IOException) {
            return@withContext NetworkState.NetworkException(error.message!!)
        }
    }
}
