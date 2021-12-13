package com.example.pinterbest.domain.usecases

import com.example.pinterbest.domain.entities.BoardCreation
import com.example.pinterbest.domain.repositories.BoardRepository
import javax.inject.Inject

class PostNewBoardUseCase @Inject constructor(
    private val boardRepository: BoardRepository
) {
    suspend operator fun invoke(boardCreation: BoardCreation) = boardRepository
        .postNewBoard(boardCreation)
}
