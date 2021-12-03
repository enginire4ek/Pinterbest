package com.example.pinterbest.domain.usecases

import com.example.pinterbest.domain.repositories.AuthRepository
import javax.inject.Inject

class GetCheckAuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.getCheckAuth()
}
