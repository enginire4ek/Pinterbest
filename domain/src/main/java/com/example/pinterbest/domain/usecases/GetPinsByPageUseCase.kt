package com.example.pinterbest.domain.usecases

import com.example.pinterbest.domain.repositories.PinsRepository
import javax.inject.Inject

class GetPinsByPageUseCase @Inject constructor(
    private val pinsRepository: PinsRepository
) {
    suspend operator fun invoke() = pinsRepository.getPins()
}
