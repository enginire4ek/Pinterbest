package com.example.pinterbest.data.repository

import android.content.Context
import com.example.pinterbest.api.ApiService
import com.example.pinterbest.data.states.NetworkState
import com.example.pinterbest.utilities.SessionManager
import java.net.UnknownHostException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

class Repository(val apiService: ApiService) {

    companion object {
        const val InternalServerError = 500
    }

    fun <T> result(call: suspend () -> Response<T>): Flow<NetworkState<T?>> = flow {
        emit(NetworkState.Loading)
        try {
            val c = call()
            c.let {
                if (it.isSuccessful) {
                    emit(NetworkState.Success(it.body()))
                } else {
                    when (it.code()) {
                        InternalServerError -> {
                            it.errorBody()?.let { error ->
                                error.close()
                                emit(NetworkState.InternalServerError(error.string()))
                            }
                        }
                        else -> {
                            it.errorBody()?.let { error ->
                                error.close()
                                emit(NetworkState.Error(error.string()))
                            }
                        }
                    }
                }
            }
        } catch (e: UnknownHostException) {
            e.printStackTrace()
            emit(NetworkState.NetworkException(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    fun getpinFeed(context: Context) = result {
        apiService.getPinFeed(SessionManager(context).fetchCookie() ?: "")
    }
}
