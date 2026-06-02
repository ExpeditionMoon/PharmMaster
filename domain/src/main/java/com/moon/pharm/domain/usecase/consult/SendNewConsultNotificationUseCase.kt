package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.repository.UserRepository
import com.moon.pharm.domain.result.DataResourceResult

class SendNewConsultNotificationUseCase(
    private val userRepository: UserRepository,
    private val consultRepository: ConsultRepository
) {
    suspend operator fun invoke(pharmacistId: String, consultId: String): DataResourceResult<Unit> {
        val pharmacistResult = userRepository.getUserOnce(pharmacistId)
        if (pharmacistResult !is DataResourceResult.Success) {
            return DataResourceResult.Failure(IllegalStateException("Pharmacist user not found"))
        }

        val token = pharmacistResult.resultData.fcmToken
        if (token.isNullOrEmpty()) return DataResourceResult.Success(Unit)

        return consultRepository.sendNewConsultNotification(token, consultId)
    }
}
