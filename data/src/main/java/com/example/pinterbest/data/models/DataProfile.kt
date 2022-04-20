package com.example.pinterbest.data.models

import com.example.pinterbest.data.common.BaseUrl
import com.example.pinterbest.domain.entities.Profile
import com.google.gson.annotations.SerializedName

data class DataProfile(
    @SerializedName("ID")
    val id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("avatarLink")
    val avatarLink: String,
    @SerializedName("following")
    val following: Int,
    @SerializedName("followers")
    val followers: Int,
    @SerializedName("boardsCount")
    val boardsCount: Int,
    @SerializedName("pinsCount")
    val pinsCount: Int
)

fun DataProfile.toProfile(): Profile {
    return Profile(
        id = id,
        username = username,
        avatarLink = BaseUrl.BASE_URL_IMAGES + avatarLink,
        following = following,
        followers = followers,
        boardsCount = boardsCount,
        pinsCount = pinsCount
    )
}
