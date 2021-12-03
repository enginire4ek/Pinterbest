package com.example.pinterbest.domain.usecases

import com.example.pinterbest.domain.entities.UserLogIn
import com.example.pinterbest.domain.repositories.AuthRepository
import javax.inject.Inject

class PostLogInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(userLogIn: UserLogIn) = authRepository.postLogIn(userLogIn)
}
