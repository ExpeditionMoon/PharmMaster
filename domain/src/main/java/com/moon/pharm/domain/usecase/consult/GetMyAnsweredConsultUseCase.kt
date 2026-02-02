package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMyAnsweredConsultUseCase @Inject constructor(
    private val repository: ConsultRepository
) {
    operator fun invoke(userId: String): Flow<DataResourceResult<List<ConsultItem>>> {
        return repository.getMyAnsweredConsultList(userId)
    }
}