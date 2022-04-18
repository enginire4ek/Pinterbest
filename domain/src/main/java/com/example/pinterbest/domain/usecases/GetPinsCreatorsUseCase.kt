package com.example.pinterbest.domain.usecases

import com.example.pinterbest.domain.repositories.PinsRepository
import com.example.pinterbest.domain.repositories.ProfileRepository
import javax.inject.Inject

class GetPinsCreatorsUseCase @Inject constructor(
    private val pinsRepository: PinsRepository,
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke() = profileRepository.getPinsCreators(
        pinsRepository.getCreators()
    )
}
