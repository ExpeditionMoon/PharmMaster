package com.moon.pharm.domain.usecase.auth

import com.moon.pharm.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserIdUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): String? = authRepository.getCurrentUserId()
}
