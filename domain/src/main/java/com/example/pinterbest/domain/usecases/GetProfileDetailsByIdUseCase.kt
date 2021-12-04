package com.example.pinterbest.domain.usecases

import com.example.pinterbest.domain.repositories.ProfileRepository
import javax.inject.Inject

class GetProfileDetailsByIdUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(id: Int) = profileRepository.getProfileDetailsById(userId = id)
}
