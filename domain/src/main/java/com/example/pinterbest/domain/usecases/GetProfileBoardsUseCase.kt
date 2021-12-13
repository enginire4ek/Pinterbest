package com.example.pinterbest.domain.usecases

import com.example.pinterbest.domain.repositories.BoardRepository
import javax.inject.Inject

class GetProfileBoardsUseCase @Inject constructor(
    private val boardRepository: BoardRepository
) {
    suspend operator fun invoke(id: String) = boardRepository.getProfileBoards(id)
}
