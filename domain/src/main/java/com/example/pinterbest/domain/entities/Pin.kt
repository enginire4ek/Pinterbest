package com.example.pinterbest.domain.entities

data class Pin(
    val ID: Int,
    val userID: Int,
    var imageLink: String,
    val title: String,
    val imageHeight: Int,
    val imageWidth: Int,
    val imageAvgColor: String,
    val description: String
)
