package com.moon.pharm.domain.usecase.auth

import com.moon.pharm.domain.repository.AuthRepository

class GetCurrentUserIdUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): String? = authRepository.getCurrentUserId()
}
