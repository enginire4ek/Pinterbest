package com.example.pinterbest.domain.usecases

import com.example.pinterbest.domain.repositories.PinsRepository
import javax.inject.Inject

class GetPinDetailsByIdUseCase @Inject constructor(
    private val pinsRepository: PinsRepository
) {
    suspend operator fun invoke(pinId: Int) = pinsRepository.getPinDetails(pinId)
}
