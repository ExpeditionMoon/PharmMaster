package com.moon.pharm.domain.usecase.medication

import com.moon.pharm.domain.model.medication.Medication
import com.moon.pharm.domain.repository.MedicationRepository
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.result.mapResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMedicationsUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    operator fun invoke(userId: String): Flow<DataResourceResult<List<Medication>>> {
        return repository.getMedications(userId).map { result ->
            result.mapResult { list -> list.sortedBy { it.name } }
        }
    }
}