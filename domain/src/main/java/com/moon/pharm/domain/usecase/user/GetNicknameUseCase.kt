package com.moon.pharm.domain.usecase.user

import com.moon.pharm.domain.repository.UserRepository
import com.moon.pharm.domain.result.DataResourceResult
import javax.inject.Inject

class GetNicknameUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun getNickname(userId: String): String {
        return try {
            val result = userRepository.getUserOnce(userId)

            if (result is DataResourceResult.Success) {
                result.resultData.nickName
            } else {
                ""
            }
        } catch (e: Exception) {
            ""
        }
    }
}