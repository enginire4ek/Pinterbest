package com.example.pinterbest.presentation.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PinObjectViewData(
    val id: Int,
    val userID: Int,
    val description: String,
    val imageWidth: Int,
    val imageHeight: Int,
    val imageLink: String,
    val title: String,
    val imageAvgColor: String
) : Parcelable
