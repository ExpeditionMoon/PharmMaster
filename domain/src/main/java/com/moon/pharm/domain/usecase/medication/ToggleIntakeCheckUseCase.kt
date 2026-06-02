package com.moon.pharm.domain.usecase.medication

import com.moon.pharm.domain.model.medication.IntakeRecord
import com.moon.pharm.domain.repository.MedicationRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow

class ToggleIntakeCheckUseCase(
    private val repository: MedicationRepository
) {
    operator fun invoke(command: ToggleIntakeCommand): Flow<DataResourceResult<Unit>> {
        return if (command.isTaken) {
            repository.saveIntakeRecord(command.toIntakeRecord())
        } else {
            repository.deleteIntakeRecord(command.medicationId, command.scheduleId, command.recordDate)
        }
    }

    private fun ToggleIntakeCommand.toIntakeRecord(): IntakeRecord {
        return IntakeRecord(
            id = "",
            userId = userId,
            medicationId = medicationId,
            scheduleId = scheduleId,
            recordDate = recordDate,
            isTaken = isTaken,
            takenTime = takenTime
        )
    }
}
