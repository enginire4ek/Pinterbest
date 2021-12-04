package com.example.pinterbest.domain.entities

data class Profile(
    val id: Int,
    val username: String,
    val avatarLink: String,
    val following: Int,
    val followers: Int,
    val boardsCount: Int,
    val pinsCount: Int
)
