package com.moon.pharm.domain.usecase.auth

import com.moon.pharm.domain.repository.AuthRepository
import javax.inject.Inject

class CheckEmailDuplicateUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String): Boolean {
        return repository.isEmailDuplicated(email)
    }
}