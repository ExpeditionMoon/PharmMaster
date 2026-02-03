package com.moon.pharm.domain.usecase.medication

import com.moon.pharm.domain.model.medication.IntakeRecord
import com.moon.pharm.domain.repository.MedicationRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ToggleIntakeCheckUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    operator fun invoke(record: IntakeRecord, isTaken: Boolean): Flow<DataResourceResult<Unit>> {
        return if (isTaken) {
            repository.saveIntakeRecord(record)
        } else {
            repository.deleteIntakeRecord(record.medicationId, record.scheduleId, record.recordDate)
        }
    }
}