package com.example.pinterbest.data.models

import com.example.pinterbest.domain.entities.Pin
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

fun PinObject.toPin(): Pin {
    return Pin(
        ID = id,
        userID = userID,
        imageLink = imageLink,
        title = title,
        imageHeight = imageHeight,
        imageWidth = imageWidth,
        imageAvgColor = imageAvgColor,
        description = description
    )
}
