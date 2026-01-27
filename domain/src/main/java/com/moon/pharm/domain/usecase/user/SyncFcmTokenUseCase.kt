package com.moon.pharm.domain.usecase.user

import com.moon.pharm.domain.repository.AuthRepository
import com.moon.pharm.domain.repository.UserRepository
import javax.inject.Inject

class SyncFcmTokenUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        val currentUserId = authRepository.getCurrentUserId() ?: return

        try {
            val token = userRepository.getFcmToken()
            userRepository.updateFcmToken(currentUserId, token)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}