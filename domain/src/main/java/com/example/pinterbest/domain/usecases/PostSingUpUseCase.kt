package com.example.pinterbest.domain.usecases

import com.example.pinterbest.domain.entities.UserSignUp
import com.example.pinterbest.domain.repositories.AuthRepository
import javax.inject.Inject

class PostSingUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(userSignUp: UserSignUp) = authRepository.postSignUp(userSignUp)
}
