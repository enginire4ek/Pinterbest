package com.example.pinterbest.data.models

import com.example.pinterbest.domain.entities.UserSignUp

data class DataUserSignUp(
    val username: String,
    val email: String,
    val password: String
)

fun DataUserSignUp.toUserSignUp(): UserSignUp {
    return UserSignUp(
        username = username,
        email = email,
        password = password
    )
}
