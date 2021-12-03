package com.example.pinterbest.data.models

import com.example.pinterbest.domain.entities.UserLogIn

data class DataUserLogIn(
    val username: String,
    val password: String
)

fun DataUserLogIn.toUserLogIn(): UserLogIn {
    return UserLogIn(
        username = username,
        password = password
    )
}
