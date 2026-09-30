package com.moon.pharm.domain.usecase.medication

import com.moon.pharm.domain.model.medication.IntakeRecord
import com.moon.pharm.domain.model.medication.Medication
import com.moon.pharm.domain.model.medication.MedicationStatus
import com.moon.pharm.domain.model.medication.RepeatType
import com.moon.pharm.domain.repository.MedicationRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

class ObserveWeeklyMedicationAdherenceUseCase(
    private val repository: MedicationRepository
) {
    operator fun invoke(
        userId: String,
        today: LocalDate
    ): Flow<DataResourceResult<WeeklyMedicationAdherence>> {
        val startDate = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        return combine(
            repository.getMedications(userId),
            repository.getIntakeRecordsByRange(userId, startDate.toString(), today.toString())
        ) { medicationsResult, recordsResult ->
            when {
                medicationsResult is DataResourceResult.Success && recordsResult is DataResourceResult.Success -> {
                    DataResourceResult.Success(
                        calculateAdherence(
                            medications = medicationsResult.resultData,
                            records = recordsResult.resultData,
                            startDate = startDate,
                            endDate = today
                        )
                    )
                }
                medicationsResult is DataResourceResult.Failure -> DataResourceResult.Failure(medicationsResult.exception)
                recordsResult is DataResourceResult.Failure -> DataResourceResult.Failure(recordsResult.exception)
                else -> DataResourceResult.Loading
            }
        }
    }

    private fun calculateAdherence(
        medications: List<Medication>,
        records: List<IntakeRecord>,
        startDate: LocalDate,
        endDate: LocalDate
    ): WeeklyMedicationAdherence {
        val takenRecordKeys = records
            .asSequence()
            .filter { it.isTaken }
            .map { IntakeRecordKey(it.medicationId, it.scheduleId, it.recordDate) }
            .toSet()

        val scheduledRecordKeys = medications
            .asSequence()
            .filter { it.status == MedicationStatus.ACTIVE }
            .flatMap { medication ->
                generateSequence(startDate) { date -> date.plusDays(1) }
                    .takeWhile { date -> !date.isAfter(endDate) }
                    .filter { date -> medication.isScheduledOn(date) }
                    .flatMap { date ->
                        medication.schedules.asSequence().map { schedule ->
                            IntakeRecordKey(medication.id, schedule.id, date.toString())
                        }
                    }
            }
            .toList()

        return WeeklyMedicationAdherence(
            totalCount = scheduledRecordKeys.size,
            completedCount = scheduledRecordKeys.count(takenRecordKeys::contains)
        )
    }

    private fun Medication.isScheduledOn(date: LocalDate): Boolean {
        val start = startDate?.toLocalDate() ?: LocalDate.MIN
        if (date.isBefore(start)) return false

        val end = endDate?.toLocalDate()
        if (end != null && date.isAfter(end)) return false

        return when (repeatType) {
            RepeatType.DAILY, RepeatType.PERIOD -> true
            RepeatType.WEEKLY -> date.dayOfWeek.value in weeklyDays.orEveryDay()
        }
    }

    private fun Long.toLocalDate(): LocalDate =
        Instant.ofEpochMilli(this)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

    private fun Set<Int>.orEveryDay(): Set<Int> =
        filter { it in DayOfWeek.MONDAY.value..DayOfWeek.SUNDAY.value }
            .toSet()
            .ifEmpty { DayOfWeek.entries.map { it.value }.toSet() }

    private data class IntakeRecordKey(
        val medicationId: String,
        val scheduleId: String,
        val recordDate: String
    )
}

data class WeeklyMedicationAdherence(
    val totalCount: Int,
    val completedCount: Int
)
