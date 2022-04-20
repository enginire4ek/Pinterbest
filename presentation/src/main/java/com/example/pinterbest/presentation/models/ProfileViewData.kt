package com.example.pinterbest.presentation.models

data class ProfileViewData(
    val id: Int,
    val username: String,
    val avatarLink: String,
    val following: Int,
    val followers: Int,
    val boardsCount: Int,
    val pinsCount: Int
)
