package com.example.pinterbest.data.models

import com.example.pinterbest.domain.entities.PinInfo

data class DataPinInfo(
    val title: String,
    val description: String,
    val tags: List<String>?,
    val boardID: Int
)

fun DataPinInfo.toPinInfo(): PinInfo {
    return PinInfo(
        title = title,
        description = description,
        tags = tags,
        boardID = boardID
    )
}
