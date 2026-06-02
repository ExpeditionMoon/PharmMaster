package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteConsultAnswerUseCase @Inject constructor(
    private val consultRepository: ConsultRepository
) {
    operator fun invoke(consultId: String): Flow<DataResourceResult<Unit>> {
        return consultRepository.deleteConsultAnswer(consultId)
    }
}
