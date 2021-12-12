package com.example.pinterbest.data.models

import com.example.pinterbest.domain.entities.Board

data class DataBoard(
    val ID: Int,
    val avatarAvgColor: String,
    val avatarHeight: Int,
    val avatarLink: String,
    val avatarWidth: Int,
    val description: String,
    val title: String,
    val userID: Int
)

fun DataBoard.toBoard(): Board {
    return Board(
        ID = ID,
        avatarLink = avatarLink,
        description = description,
        title = title,
        userID = userID
    )
}
