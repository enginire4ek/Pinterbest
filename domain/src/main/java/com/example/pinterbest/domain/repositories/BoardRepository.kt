package com.example.pinterbest.domain.repositories

import com.example.pinterbest.domain.common.Result
import com.example.pinterbest.domain.entities.BoardCreation
import com.example.pinterbest.domain.entities.BoardsList
import com.example.pinterbest.domain.entities.IdEntity
import kotlinx.coroutines.flow.Flow

interface BoardRepository {
    suspend fun postNewBoard(board: BoardCreation): Flow<Result<IdEntity>>

    suspend fun getProfileBoards(id: String): Flow<Result<BoardsList>>
}
