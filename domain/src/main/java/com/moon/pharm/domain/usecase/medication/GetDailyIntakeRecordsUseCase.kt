package com.moon.pharm.domain.usecase.medication

import com.moon.pharm.domain.model.medication.IntakeRecord
import com.moon.pharm.domain.repository.MedicationRepository
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.result.mapResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDailyIntakeRecordsUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    operator fun invoke(userId: String, date: String): Flow<DataResourceResult<List<IntakeRecord>>> {
        return repository.getIntakeRecords(userId, date).map { result ->
            result.mapResult { list ->
                list.sortedBy { it.scheduleId }
            }
        }
    }
}