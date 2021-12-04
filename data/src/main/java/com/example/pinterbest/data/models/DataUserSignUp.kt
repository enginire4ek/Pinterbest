package com.example.pinterbest.data.models

import com.example.pinterbest.domain.entities.UserSignUp
import com.google.gson.annotations.SerializedName

data class DataUserSignUp(
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)

fun DataUserSignUp.toUserSignUp(): UserSignUp {
    return UserSignUp(
        username = username,
        email = email,
        password = password
    )
}
