package com.moon.pharm.domain.usecase.user

import com.moon.pharm.domain.repository.AuthRepository
import com.moon.pharm.domain.repository.UserRepository
import com.moon.pharm.domain.result.DataResourceResult
import javax.inject.Inject

class SyncFcmTokenUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        val currentUserId = authRepository.getCurrentUserId() ?: return

        try {
            val token = userRepository.getFcmToken()
            val userResult = userRepository.getUserOnce(currentUserId)
            if (userResult is DataResourceResult.Success) {
                val updatedUser = userResult.resultData.copy(fcmToken = token)
                userRepository.saveUser(updatedUser)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}