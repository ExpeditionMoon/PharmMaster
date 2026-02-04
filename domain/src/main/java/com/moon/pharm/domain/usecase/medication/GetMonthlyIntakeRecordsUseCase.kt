package com.moon.pharm.domain.usecase.medication

import com.moon.pharm.domain.model.medication.IntakeRecord
import com.moon.pharm.domain.repository.MedicationRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth
import javax.inject.Inject

class GetMonthlyIntakeRecordsUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    operator fun invoke(userId: String, yearMonthString: String): Flow<DataResourceResult<List<IntakeRecord>>> {
        val yearMonth = YearMonth.parse(yearMonthString)

        val startDate = yearMonth.atDay(1).toString()
        val endDate = yearMonth.atEndOfMonth().toString()

        return repository.getIntakeRecordsByRange(userId, startDate, endDate)
    }
}