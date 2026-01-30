package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.result.DataResourceResult
import javax.inject.Inject

class SendAnswerNotificationUseCase @Inject constructor(
    private val repository: ConsultRepository
) {
    suspend operator fun invoke(targetToken: String, consultId: String): DataResourceResult<Unit> {
        return repository.sendAnswerNotification(targetToken, consultId)
    }
}