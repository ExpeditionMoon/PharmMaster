package com.moon.pharm.domain.usecase.user

import com.moon.pharm.domain.repository.UserRepository

class CheckEmailDuplicatedUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String): Boolean {
        return userRepository.isEmailDuplicated(email)
    }
}
