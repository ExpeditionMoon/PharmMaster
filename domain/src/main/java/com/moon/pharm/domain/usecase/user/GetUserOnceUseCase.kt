package com.moon.pharm.domain.usecase.user

import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.repository.UserRepository
import com.moon.pharm.domain.result.DataResourceResult

class GetUserOnceUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String): DataResourceResult<User> {
        return userRepository.getUserOnce(userId)
    }
}
