package com.moon.pharm.domain.usecase.medication

import com.moon.pharm.domain.model.MedicationItem
import com.moon.pharm.domain.repository.MedicationRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMedicationItemsUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    operator fun invoke(): Flow<DataResourceResult<List<MedicationItem>>> {
        return repository.getMedicationItems()
    }
}