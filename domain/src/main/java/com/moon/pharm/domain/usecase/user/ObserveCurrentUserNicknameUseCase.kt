package com.moon.pharm.domain.usecase.user

import com.moon.pharm.domain.repository.AuthRepository
import com.moon.pharm.domain.repository.UserRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class ObserveCurrentUserNicknameUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<DataResourceResult<String>> {
        val userId = authRepository.getCurrentUserId()
            ?: return flowOf(DataResourceResult.Failure(IllegalStateException("User not logged in")))

        return userRepository.getUser(userId).map { result ->
            when (result) {
                is DataResourceResult.Success -> DataResourceResult.Success(result.resultData.nickName)
                is DataResourceResult.Failure -> DataResourceResult.Failure(result.exception)
                is DataResourceResult.Loading -> DataResourceResult.Loading
            }
        }
    }
}
