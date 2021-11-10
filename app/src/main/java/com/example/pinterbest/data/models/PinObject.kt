package com.example.pinterbest.data.models

import com.google.gson.annotations.SerializedName

data class PinObject(
    @SerializedName("ID")
    val id: Int,
    val userID: Int,
    val imageLink: String,
    val title: String,
    val imageHeight: Int,
    val imageWidth: Int,
    val imageAvgColor: String,
    val description: String,
    val creationDate: String,
    val reportsCount: Int
)
