package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow

class UpdateConsultUseCase(
    private val consultRepository: ConsultRepository
) {
    operator fun invoke(
        consultId: String,
        title: String,
        content: String,
        isPublic: Boolean
    ): Flow<DataResourceResult<Unit>> {
        return consultRepository.updateConsult(consultId, title, content, isPublic)
    }
}
