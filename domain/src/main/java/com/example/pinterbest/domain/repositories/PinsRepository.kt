package com.example.pinterbest.domain.repositories

import com.example.pinterbest.domain.common.Result
import com.example.pinterbest.domain.entities.IdEntity
import com.example.pinterbest.domain.entities.PinInfo
import com.example.pinterbest.domain.entities.PinsList
import kotlinx.coroutines.flow.Flow

interface PinsRepository {
    suspend fun getPins(): Flow<Result<PinsList>>

    suspend fun getPinsByBoardId(boardId: Int): Flow<Result<PinsList>>

    suspend fun postPin(pinInfo: PinInfo, pinImage: ByteArray): Flow<Result<IdEntity>>
}
