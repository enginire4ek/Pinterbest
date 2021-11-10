package com.example.pinterbest.data.states

sealed class NetworkState<out T> {
    data class Success<out R>(val data: R) : NetworkState<R>()
    object Loading : NetworkState<Nothing>()
    data class Error(val error: String) : NetworkState<Nothing>()
    data class NetworkException(val error: String) : NetworkState<Nothing>()
    data class InternalServerError(val exception: String) : NetworkState<Nothing>()
}
