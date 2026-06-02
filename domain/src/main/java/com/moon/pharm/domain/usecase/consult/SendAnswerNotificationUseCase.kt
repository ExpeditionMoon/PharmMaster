package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.repository.UserRepository
import com.moon.pharm.domain.result.DataResourceResult
import javax.inject.Inject

class SendAnswerNotificationUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val consultRepository: ConsultRepository
) {
    suspend operator fun invoke(userId: String, consultId: String): DataResourceResult<Unit> {
        val userResult = userRepository.getUserOnce(userId)
        if (userResult !is DataResourceResult.Success) {
            return DataResourceResult.Failure(IllegalStateException("User not found"))
        }

        val token = userResult.resultData.fcmToken
        if (token.isNullOrEmpty()) return DataResourceResult.Success(Unit)

        return consultRepository.sendAnswerNotification(token, consultId)
    }
}
