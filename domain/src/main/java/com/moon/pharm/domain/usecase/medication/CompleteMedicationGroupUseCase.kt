package com.moon.pharm.domain.usecase.medication

import com.moon.pharm.domain.model.medication.IntakeRecord
import com.moon.pharm.domain.repository.MedicationRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

/** Saves one intake record per medication schedule after a group-level confirmation. */
class CompleteMedicationGroupUseCase(
    private val medicationRepository: MedicationRepository
) {
    operator fun invoke(command: CompleteMedicationGroupCommand): Flow<DataResourceResult<Unit>> = flow {
        emit(DataResourceResult.Loading)

        for (item in command.items.distinct()) {
            when (val result = medicationRepository.saveIntakeRecord(item.toIntakeRecord(command))
                .filter { it !is DataResourceResult.Loading }
                .first()) {
                is DataResourceResult.Success -> Unit
                is DataResourceResult.Failure -> {
                    emit(DataResourceResult.Failure(result.exception))
                    return@flow
                }
                DataResourceResult.Loading -> Unit
            }
        }

        emit(DataResourceResult.Success(Unit))
    }

    private fun MedicationGroupIntakeItem.toIntakeRecord(command: CompleteMedicationGroupCommand) = IntakeRecord(
        id = "",
        userId = command.userId,
        medicationId = medicationId,
        scheduleId = scheduleId,
        recordDate = command.recordDate,
        isTaken = true,
        takenTime = command.takenTime
    )
}
