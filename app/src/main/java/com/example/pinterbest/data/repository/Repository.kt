package com.example.pinterbest.data.repository

import android.content.SharedPreferences
import com.example.pinterbest.api.ApiClient
import com.example.pinterbest.data.models.User
import com.example.pinterbest.data.models.UserLogin
import com.example.pinterbest.data.states.NetworkState
import java.net.UnknownHostException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Response

class Repository(val preferences: SharedPreferences) {

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

    fun getpinFeed() = result {
        ApiClient().getInstance().getClient()
            .getPinFeed(SessionRepository(preferences).fetchCookie() ?: "")
    }

    fun getProfile() = result {
        ApiClient().getInstance().getClient()
            .getProfile(SessionRepository(preferences).fetchCookie() ?: "")
    }

    suspend fun postsignUp(user: User) = withContext(Dispatchers.IO) {
        ApiClient().getInstance().getClient()
            .postSignUp(userData = user)
    }

    suspend fun postlogIn(user: UserLogin) = withContext(Dispatchers.IO) {
        ApiClient().getInstance().getClient()
            .postLogIn(userData = user)
    }

    suspend fun getauthCheck(): Int {
        return ApiClient().getInstance().getClient()
            .getAuthCheck(SessionRepository(preferences).fetchCookie() ?: "").code()
    }
}
