package com.moon.pharm.domain.usecase.auth

import com.moon.pharm.domain.repository.UserRepository
import javax.inject.Inject

class CheckEmailDuplicateUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(email: String): Boolean {
        return repository.isEmailDuplicated(email)
    }
}