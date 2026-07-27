package com.moon.pharm.domain.usecase.auth

import com.moon.pharm.domain.repository.AuthRepository
import com.moon.pharm.domain.result.DataResourceResult

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): DataResourceResult<String> {
        return authRepository.login(email, password)
    }
}
