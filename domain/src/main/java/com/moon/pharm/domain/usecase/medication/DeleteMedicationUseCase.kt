package com.moon.pharm.domain.usecase.medication

import com.moon.pharm.domain.alarm.AlarmScheduler
import com.moon.pharm.domain.repository.MedicationRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class DeleteMedicationUseCase(
    private val medicationRepository: MedicationRepository,
    private val alarmScheduler: AlarmScheduler
) {
    operator fun invoke(medicationId: String): Flow<DataResourceResult<Unit>> {
        return medicationRepository.deleteMedication(medicationId)
            .onEach { result ->
                if (result is DataResourceResult.Success) alarmScheduler.cancel(medicationId)
            }
    }
}
