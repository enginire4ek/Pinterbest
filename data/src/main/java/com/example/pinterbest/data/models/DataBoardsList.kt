package com.example.pinterbest.data.models

import com.example.pinterbest.domain.entities.BoardsList

data class DataBoardsList(
    val boards: List<DataBoard>
)

fun DataBoardsList.toBoardsList(): BoardsList {
    return BoardsList(
        boards = boards.map {
            it.toBoard()
        }
    )
}
