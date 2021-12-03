package com.example.pinterbest.domain.usecases

import com.example.pinterbest.domain.repositories.SessionRepository
import javax.inject.Inject

class SaveSessionToPrefsUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    operator fun invoke(cookie: String) = sessionRepository.saveSession(cookie)
}
