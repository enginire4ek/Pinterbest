package com.example.pinterbest.domain.entities

data class PinInfo(
    val title: String,
    val description: String,
    val tags: List<String>?,
    val boardID: Int
)
