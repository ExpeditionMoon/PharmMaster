package com.moon.pharm.domain.usecase.medication

import com.moon.pharm.domain.model.medication.IntakeRecord
import com.moon.pharm.domain.model.medication.Medication
import com.moon.pharm.domain.repository.MedicationRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

class GetMedicationHistoryItemsUseCase(
    private val repository: MedicationRepository
) {
    operator fun invoke(userId: String, yearMonth: YearMonth): Flow<DataResourceResult<Map<String, List<MedicationHistoryItem>>>> {
        val startDate = yearMonth.atDay(1).toString()
        val endDate = yearMonth.atEndOfMonth().toString()

        return combine(
            repository.getIntakeRecordsByRange(userId, startDate, endDate),
            repository.getMedications(userId)
        ) { recordsResult, medicationsResult ->
            when {
                recordsResult is DataResourceResult.Success && medicationsResult is DataResourceResult.Success -> {
                    DataResourceResult.Success(
                        generateHistoryItems(
                            yearMonth = yearMonth,
                            medications = medicationsResult.resultData,
                            realRecords = recordsResult.resultData
                        )
                    )
                }
                recordsResult is DataResourceResult.Failure -> DataResourceResult.Failure(recordsResult.exception)
                medicationsResult is DataResourceResult.Failure -> DataResourceResult.Failure(medicationsResult.exception)
                else -> DataResourceResult.Loading
            }
        }
    }

    private fun generateHistoryItems(
        yearMonth: YearMonth,
        medications: List<Medication>,
        realRecords: List<IntakeRecord>
    ): Map<String, List<MedicationHistoryItem>> {
        val result = mutableMapOf<String, MutableList<MedicationHistoryItem>>()
        var currentDate = yearMonth.atDay(1)

        while (!currentDate.isAfter(yearMonth.atEndOfMonth())) {
            val dateKey = currentDate.toString()
            val dailyItems = mutableListOf<MedicationHistoryItem>()

            medications.forEach { medication ->
                if (medication.isActiveOn(currentDate)) {
                    medication.schedules.forEach { schedule ->
                        val record = realRecords.find {
                            it.medicationId == medication.id &&
                                    it.recordDate == dateKey &&
                                    it.scheduleId == schedule.id &&
                                    it.isTaken
                        } ?: IntakeRecord(
                            id = "virtual_${medication.id}_${schedule.id}_$dateKey",
                            userId = medication.userId,
                            medicationId = medication.id,
                            scheduleId = schedule.id,
                            recordDate = dateKey,
                            isTaken = false,
                            takenTime = null
                        )

                        dailyItems.add(
                            MedicationHistoryItem(
                                recordId = record.id,
                                medicationId = record.medicationId,
                                scheduleId = record.scheduleId,
                                recordDate = record.recordDate,
                                isTaken = record.isTaken,
                                takenTime = record.takenTime,
                                medicationName = medication.name,
                                time = schedule.time
                            )
                        )
                    }
                }
            }

            result[dateKey] = dailyItems.sortedBy { it.time }.toMutableList()
            currentDate = currentDate.plusDays(1)
        }

        return result
    }

    private fun Medication.isActiveOn(date: LocalDate): Boolean {
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
