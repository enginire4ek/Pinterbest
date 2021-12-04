package com.example.pinterbest.domain.common

import java.lang.Exception

sealed class Result<out T> {
    class Success<R>(val data: R) : Result<R>()
    class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
