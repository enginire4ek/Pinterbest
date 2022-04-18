package com.example.pinterbest.data.repository

import com.example.pinterbest.data.api.ApiService
import com.example.pinterbest.data.common.ErrorMessage
import com.example.pinterbest.data.models.toBoardsList
import com.example.pinterbest.data.models.toIdEntity
import com.example.pinterbest.domain.common.Result
import com.example.pinterbest.domain.entities.BoardCreation
import com.example.pinterbest.domain.entities.BoardsList
import com.example.pinterbest.domain.entities.IdEntity
import com.example.pinterbest.domain.repositories.BoardRepository
import com.example.pinterbest.domain.repositories.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

class BoardRepositoryImpl
@Inject constructor(
    private val sessionRepository: SessionRepository,
    private val authClient: ApiService
) : BoardRepository {
    override suspend fun postNewBoard(board: BoardCreation): Flow<Result<IdEntity>> = flow {
        try {
            emit(Result.Loading)
            val response = authClient
                .postNewBoard(board, sessionRepository.authProvider() ?: "")
                .toIdEntity()
            emit(Result.Success(response))
        } catch (e: HttpException) {
            if (ErrorMessage.ErrorMap[e.code()] != null) {
                emit(Result.Error(e))
            } else {
                emit(Result.Error(UnknownHostException()))
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getProfileBoards(id: String): Flow<Result<BoardsList>> = flow {
        try {
            emit(Result.Loading)
            val boards = authClient
                .getProfileBoards(id)
                .toBoardsList()
            emit(Result.Success(boards))
        } catch (e: HttpException) {
            if (ErrorMessage.ErrorMap[e.code()] != null) {
                emit(Result.Error(e))
            } else {
                emit(Result.Error(UnknownHostException()))
            }
        }
    }.flowOn(Dispatchers.IO)
}
