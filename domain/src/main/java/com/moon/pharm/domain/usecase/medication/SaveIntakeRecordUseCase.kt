package com.moon.pharm.domain.usecase.medication

import com.moon.pharm.domain.model.medication.IntakeRecord
import com.moon.pharm.domain.repository.MedicationRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveIntakeRecordUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    operator fun invoke(record: IntakeRecord): Flow<DataResourceResult<Unit>> {
        return repository.saveIntakeRecord(record)
    }
}