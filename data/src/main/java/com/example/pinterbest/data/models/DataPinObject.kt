package com.example.pinterbest.data.models

import com.example.pinterbest.data.common.BaseUrl
import com.example.pinterbest.domain.entities.Pin
import com.google.gson.annotations.SerializedName

data class DataPinObject(
    @SerializedName("ID")
    val id: Int,
    @SerializedName("userID")
    val userID: Int,
    @SerializedName("imageLink")
    val imageLink: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("imageHeight")
    val imageHeight: Int,
    @SerializedName("imageWidth")
    val imageWidth: Int,
    @SerializedName("imageAvgColor")
    val imageAvgColor: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("creationDate")
    val creationDate: String,
    @SerializedName("reportsCount")
    val reportsCount: Int
)

fun DataPinObject.toPin(): Pin {
    return Pin(
        ID = id,
        userID = userID,
        imageLink = BaseUrl.BASE_URL_IMAGES + imageLink,
        title = title,
        imageHeight = imageHeight,
        imageWidth = imageWidth,
        imageAvgColor = imageAvgColor,
        description = description
    )
}
