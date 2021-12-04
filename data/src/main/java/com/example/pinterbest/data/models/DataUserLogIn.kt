package com.example.pinterbest.data.models

import com.example.pinterbest.domain.entities.UserLogIn
import com.google.gson.annotations.SerializedName

data class DataUserLogIn(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String
)

fun DataUserLogIn.toUserLogIn(): UserLogIn {
    return UserLogIn(
        username = username,
        password = password
    )
}
