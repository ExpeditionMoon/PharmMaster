package com.moon.pharm.domain.usecase.auth

import com.moon.pharm.domain.repository.AuthRepository
import com.moon.pharm.domain.result.DataResourceResult

class LogoutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): DataResourceResult<Unit> {
        return authRepository.logout()
    }
}
