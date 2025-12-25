package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.model.ConsultItem
import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow

class GetConsultItemsUseCase(private val repository: ConsultRepository) {
    operator fun invoke(): Flow<DataResourceResult<List<ConsultItem>>> {
        return repository.getConsultItems()
    }
}