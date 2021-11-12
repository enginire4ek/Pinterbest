package com.example.pinterbest.data.models

import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("ID")
    val id: Int,
    val username: String,
    val email: String,
    val avatarLink: String,
    val following: Int,
    val followers: Int,
    val boardsCount: Int,
    val pinsCount: Int
)
