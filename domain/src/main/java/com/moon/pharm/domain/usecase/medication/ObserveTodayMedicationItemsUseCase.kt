package com.moon.pharm.domain.usecase.medication

import com.moon.pharm.domain.model.medication.IntakeRecord
import com.moon.pharm.domain.model.medication.MealTiming
import com.moon.pharm.domain.model.medication.Medication
import com.moon.pharm.domain.model.medication.MedicationStatus
import com.moon.pharm.domain.model.medication.MedicationType
import com.moon.pharm.domain.model.medication.RepeatType
import com.moon.pharm.domain.repository.MedicationRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class ObserveTodayMedicationItemsUseCase(
    private val repository: MedicationRepository
) {
    operator fun invoke(userId: String, date: LocalDate): Flow<DataResourceResult<List<MedicationScheduleItem>>> {
        val dateString = date.toString()
        return combine(
            repository.getMedications(userId),
            repository.getIntakeRecords(userId, dateString)
        ) { medicationsResult, recordsResult ->
            when {
                medicationsResult is DataResourceResult.Success && recordsResult is DataResourceResult.Success -> {
                    DataResourceResult.Success(
                        medicationsResult.resultData
                            .filter { it.isVisibleOn(date) }
                            .toMedicationScheduleItems(recordsResult.resultData)
                    )
                }
                medicationsResult is DataResourceResult.Failure -> DataResourceResult.Failure(medicationsResult.exception)
                recordsResult is DataResourceResult.Failure -> DataResourceResult.Failure(recordsResult.exception)
                else -> DataResourceResult.Loading
            }
        }
    }

    private fun List<Medication>.toMedicationScheduleItems(records: List<IntakeRecord>): List<MedicationScheduleItem> {
        return flatMap { medication ->
            medication.schedules.map { schedule ->
                MedicationScheduleItem(
                    medicationId = medication.id,
                    scheduleId = schedule.id,
                    name = medication.name,
                    type = medication.type.toInput(),
                    repeatType = medication.repeatType.toInput(),
                    time = schedule.time,
                    dosage = schedule.dosage,
                    mealTiming = schedule.mealTiming.toInput(),
                    isPaused = medication.status == MedicationStatus.PAUSED,
                    isTaken = records.any { record ->
                        record.medicationId == medication.id &&
                                record.scheduleId == schedule.id &&
                                record.isTaken
                    }
                )
            }
        }.sortedBy { it.time }
    }

    private fun Medication.isVisibleOn(date: LocalDate): Boolean {
        if (status == MedicationStatus.ENDED) return false
        val start = startDate?.toLocalDate() ?: LocalDate.MIN
        if (date.isBefore(start)) return false

        val end = endDate?.toLocalDate()
        return end == null || !date.isAfter(end)
    }

    private fun Long.toLocalDate(): LocalDate {
        return Instant.ofEpochMilli(this)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
}

internal fun MedicationType.toInput(): MedicationTypeInput {
    return when (this) {
        MedicationType.PRESCRIPTION -> MedicationTypeInput.PRESCRIPTION
        MedicationType.OTC -> MedicationTypeInput.OTC
        MedicationType.SUPPLEMENT -> MedicationTypeInput.SUPPLEMENT
    }
}

internal fun MealTiming.toInput(): MealTimingInput {
    return when (this) {
        MealTiming.BEFORE_MEAL -> MealTimingInput.BEFORE_MEAL
        MealTiming.DURING_MEAL -> MealTimingInput.DURING_MEAL
        MealTiming.AFTER_MEAL -> MealTimingInput.AFTER_MEAL
        MealTiming.NONE -> MealTimingInput.NONE
    }
}

internal fun RepeatType.toInput(): RepeatTypeInput {
    return when (this) {
        RepeatType.DAILY -> RepeatTypeInput.DAILY
        RepeatType.WEEKLY -> RepeatTypeInput.WEEKLY
        RepeatType.PERIOD -> RepeatTypeInput.PERIOD
    }
}
