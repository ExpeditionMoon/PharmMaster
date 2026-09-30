package com.moon.pharm.domain.usecase.medication

import com.moon.pharm.domain.alarm.AlarmScheduler
import com.moon.pharm.domain.model.medication.MedicationStatus
import com.moon.pharm.domain.repository.MedicationRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

class DeleteMedicationUseCase(
    private val medicationRepository: MedicationRepository,
    private val alarmScheduler: AlarmScheduler
) {
    /** Compatibility entry point for call sites that do not have the user id. */
    operator fun invoke(medicationId: String): Flow<DataResourceResult<Unit>> =
        medicationRepository.deleteMedication(medicationId).onEach { result ->
            if (result is DataResourceResult.Success) alarmScheduler.cancel(medicationId)
        }

    operator fun invoke(command: DeleteMedicationCommand): Flow<DataResourceResult<Unit>> = flow {
        emit(DataResourceResult.Loading)
        when (val result = medicationRepository.getMedications(command.userId)
            .filter { it !is DataResourceResult.Loading }
            .first()) {
            is DataResourceResult.Failure -> emit(DataResourceResult.Failure(result.exception))
            is DataResourceResult.Success -> {
                val medication = result.resultData.find { it.id == command.medicationId }
                medicationRepository.deleteMedication(command.medicationId).collect { deleteResult ->
                    emit(deleteResult)
                    if (deleteResult is DataResourceResult.Success) {
                        val groupId = medication?.intakeGroupId
                        val remainingGroupMember = groupId?.let { id ->
                            result.resultData.firstOrNull { candidate ->
                                candidate.id != medication.id &&
                                    candidate.intakeGroupId == id &&
                                    candidate.status == MedicationStatus.ACTIVE
                            }
                        }
                        if (remainingGroupMember != null) {
                            alarmScheduler.schedule(remainingGroupMember)
                        } else {
                            alarmScheduler.cancel(groupId ?: command.medicationId)
                        }
                    }
                }
            }
            DataResourceResult.Loading -> Unit
        }
    }
}
