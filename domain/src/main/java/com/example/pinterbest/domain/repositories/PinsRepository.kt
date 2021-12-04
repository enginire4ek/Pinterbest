package com.example.pinterbest.domain.repositories

import com.example.pinterbest.domain.common.Result
import com.example.pinterbest.domain.entities.Pin
import com.example.pinterbest.domain.entities.PinsList
import kotlinx.coroutines.flow.Flow

interface PinsRepository {
    suspend fun getPins(): Flow<Result<PinsList>>

    suspend fun getPinDetails(pinId: Int): Flow<Result<Pin>>
}
