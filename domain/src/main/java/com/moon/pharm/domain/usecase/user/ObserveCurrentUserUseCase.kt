package com.moon.pharm.domain.usecase.user

import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.repository.AuthRepository
import com.moon.pharm.domain.repository.UserRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ObserveCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<DataResourceResult<User>> {
        val userId = authRepository.getCurrentUserId()
            ?: return flowOf(DataResourceResult.Failure(IllegalStateException("User not logged in")))

        return userRepository.getUser(userId)
    }
}
