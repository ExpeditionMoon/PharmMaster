package com.moon.pharm.domain.usecase.user

import com.moon.pharm.domain.repository.AuthRepository
import com.moon.pharm.domain.repository.UserRepository
import com.moon.pharm.domain.result.DataResourceResult

class SyncFcmTokenUseCase(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(token: String? = null) {
        val currentUserId = authRepository.getCurrentUserId() ?: return

        try {
            val fcmToken = token ?: userRepository.getFcmToken()
            val userResult = userRepository.getUserOnce(currentUserId)
            if (userResult is DataResourceResult.Success) {
                val updatedUser = userResult.resultData.copy(fcmToken = fcmToken)
                userRepository.saveUser(updatedUser)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
