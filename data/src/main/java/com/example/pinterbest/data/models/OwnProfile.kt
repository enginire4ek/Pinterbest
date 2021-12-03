package com.example.pinterbest.data.models

import com.example.pinterbest.domain.entities.Profile
import com.google.gson.annotations.SerializedName

data class OwnProfile(
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

fun OwnProfile.toProfile(): Profile {
    return Profile(
        id = id,
        username = username,
        avatarLink = avatarLink,
        following = following,
        followers = followers,
        boardsCount = boardsCount,
        pinsCount = pinsCount
    )
}
