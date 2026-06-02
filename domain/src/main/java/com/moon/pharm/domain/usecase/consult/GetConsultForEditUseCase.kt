package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetConsultForEditUseCase @Inject constructor(
    private val consultRepository: ConsultRepository
) {
    operator fun invoke(consultId: String): Flow<DataResourceResult<ConsultItem>> {
        return consultRepository.getConsultDetail(consultId)
    }
}
