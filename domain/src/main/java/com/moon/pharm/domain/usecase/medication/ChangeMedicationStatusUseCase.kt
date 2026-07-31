package com.moon.pharm.domain.usecase.medication

import com.moon.pharm.domain.alarm.AlarmScheduler
import com.moon.pharm.domain.model.medication.Medication
import com.moon.pharm.domain.model.medication.MedicationException
import com.moon.pharm.domain.model.medication.MedicationStatus
import com.moon.pharm.domain.repository.MedicationRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class ChangeMedicationStatusUseCase(
    private val medicationRepository: MedicationRepository,
    private val alarmScheduler: AlarmScheduler
) {
    operator fun invoke(command: ChangeMedicationStatusCommand): Flow<DataResourceResult<Unit>> = flow {
        emit(DataResourceResult.Loading)

        when (val medicationsResult = medicationRepository.getMedications(command.userId)
            .filter { it !is DataResourceResult.Loading }
            .first()) {
            is DataResourceResult.Failure -> emit(DataResourceResult.Failure(medicationsResult.exception))
            is DataResourceResult.Success -> {
                val medication = medicationsResult.resultData.find { it.id == command.medicationId }
                if (medication == null) {
                    emit(DataResourceResult.Failure(MedicationException.NotFound()))
                    return@flow
                }

                val updatedMedication = medication.withStatus(command)
                medicationRepository.saveMedication(updatedMedication).collect { saveResult ->
                    emit(saveResult)
                    if (saveResult is DataResourceResult.Success) {
                        when (updatedMedication.status) {
                            MedicationStatus.ACTIVE -> alarmScheduler.schedule(updatedMedication)
                            MedicationStatus.PAUSED,
                            MedicationStatus.ENDED -> alarmScheduler.cancel(updatedMedication.id)
                        }
                    }
                }
            }
            DataResourceResult.Loading -> Unit
        }
    }

    private fun Medication.withStatus(command: ChangeMedicationStatusCommand): Medication {
        val newStatus = command.status.toDomain()
        return copy(
            status = newStatus,
            endDate = when (newStatus) {
                MedicationStatus.ENDED -> endDate?.coerceAtMost(command.changedAt) ?: command.changedAt
                else -> endDate
            }
        )
    }

    private fun MedicationStatusInput.toDomain(): MedicationStatus = when (this) {
        MedicationStatusInput.ACTIVE -> MedicationStatus.ACTIVE
        MedicationStatusInput.PAUSED -> MedicationStatus.PAUSED
        MedicationStatusInput.ENDED -> MedicationStatus.ENDED
    }
}
