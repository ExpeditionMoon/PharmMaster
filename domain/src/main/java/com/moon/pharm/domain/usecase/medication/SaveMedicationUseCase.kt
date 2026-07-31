package com.moon.pharm.domain.usecase.medication

import com.moon.pharm.domain.alarm.AlarmScheduler
import com.moon.pharm.domain.model.medication.MealTiming
import com.moon.pharm.domain.model.medication.Medication
import com.moon.pharm.domain.model.medication.MedicationSchedule
import com.moon.pharm.domain.model.medication.MedicationType
import com.moon.pharm.domain.model.medication.RepeatType
import com.moon.pharm.domain.repository.MedicationRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import java.util.UUID

class SaveMedicationUseCase(
    private val medicationRepository: MedicationRepository,
    private val alarmScheduler: AlarmScheduler
) {
    operator fun invoke(command: SaveMedicationCommand): Flow<DataResourceResult<Unit>> {
        val medication = command.toMedication()
        return medicationRepository.saveMedication(medication)
            .onEach { result ->
                if (result is DataResourceResult.Success) {
                    alarmScheduler.schedule(medication)
                }
            }
    }

    private fun SaveMedicationCommand.toMedication(): Medication {
        return Medication(
            id = medicationId ?: UUID.randomUUID().toString(),
            userId = userId,
            name = name,
            type = type.toMedicationType(),
            startDate = startDate,
            endDate = endDate,
            repeatType = repeatType.toRepeatType(),
            weeklyDays = weeklyDays,
            schedules = schedules.map { it.toMedicationSchedule() },
            isGrouped = isGrouped,
            isAlarmEnabled = isAlarmEnabled
        )
    }

    private fun MedicationScheduleCommand.toMedicationSchedule(): MedicationSchedule {
        return MedicationSchedule(
            id = scheduleId ?: UUID.randomUUID().toString(),
            time = time,
            dosage = dosage,
            mealTiming = mealTiming.toMealTiming()
        )
    }

    private fun MedicationTypeInput.toMedicationType(): MedicationType {
        return when (this) {
            MedicationTypeInput.PRESCRIPTION -> MedicationType.PRESCRIPTION
            MedicationTypeInput.OTC -> MedicationType.OTC
            MedicationTypeInput.SUPPLEMENT -> MedicationType.SUPPLEMENT
        }
    }

    private fun MealTimingInput.toMealTiming(): MealTiming {
        return when (this) {
            MealTimingInput.BEFORE_MEAL -> MealTiming.BEFORE_MEAL
            MealTimingInput.DURING_MEAL -> MealTiming.DURING_MEAL
            MealTimingInput.AFTER_MEAL -> MealTiming.AFTER_MEAL
            MealTimingInput.NONE -> MealTiming.NONE
        }
    }

    private fun RepeatTypeInput.toRepeatType(): RepeatType {
        return when (this) {
            RepeatTypeInput.DAILY -> RepeatType.DAILY
            RepeatTypeInput.WEEKLY -> RepeatType.WEEKLY
            RepeatTypeInput.PERIOD -> RepeatType.PERIOD
        }
    }
}
