package com.example.pinterbest.domain.usecases

import com.example.pinterbest.domain.entities.PinInfo
import com.example.pinterbest.domain.repositories.PinsRepository
import javax.inject.Inject

class PostPinUseCase @Inject constructor(
    private val pinsRepository: PinsRepository
) {
    suspend operator fun invoke(pinInfo: PinInfo, pinImage: ByteArray) = pinsRepository.postPin(
        pinInfo,
        pinImage
    )
}
