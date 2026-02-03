package com.moon.pharm.domain.usecase.medication

import com.moon.pharm.domain.repository.MedicationRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteIntakeRecordUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    operator fun invoke(
        medicationId: String,
        scheduleId: String,
        date: String
    ): Flow<DataResourceResult<Unit>> {
        return repository.deleteIntakeRecord(medicationId, scheduleId, date)
    }
}