package com.moon.pharm.domain.usecase.medication

import com.moon.pharm.domain.alarm.AlarmScheduler
import com.moon.pharm.domain.repository.MedicationRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.first

class RestoreMedicationAlarmsUseCase(
    private val medicationRepository: MedicationRepository,
    private val alarmScheduler: AlarmScheduler
) {
    suspend operator fun invoke(userId: String) {
        val result = medicationRepository.getMedications(userId)
            .first { it !is DataResourceResult.Loading }

        if (result is DataResourceResult.Success) {
            result.resultData.forEach(alarmScheduler::schedule)
        }
    }
}
