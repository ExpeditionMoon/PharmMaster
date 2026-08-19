package com.moon.pharm.data.mapper

import com.moon.pharm.data.common.toTimestamp
import com.moon.pharm.data.datasource.remote.dto.MedicationDTO
import com.moon.pharm.data.datasource.remote.dto.MedicationScheduleDTO
import com.moon.pharm.domain.model.medication.MealInterval
import com.moon.pharm.domain.model.medication.MealSlot
import com.moon.pharm.domain.model.medication.MealTiming
import com.moon.pharm.domain.model.medication.Medication
import com.moon.pharm.domain.model.medication.MedicationSchedule
import com.moon.pharm.domain.model.medication.MedicationScheduleBasis
import com.moon.pharm.domain.model.medication.MedicationStatus
import com.moon.pharm.domain.model.medication.MedicationType
import com.moon.pharm.domain.model.medication.RepeatType

fun MedicationScheduleDTO.toDomain(): MedicationSchedule = MedicationSchedule(
    id = id,
    time = time,
    dosage = dosage,
    mealTiming = MealTiming.from(mealTiming),
    basis = basis.toScheduleBasis(),
    mealSlot = mealSlot?.let { value -> MealSlot.entries.find { it.name == value } },
    mealInterval = mealInterval.toMealInterval()
)

fun MedicationSchedule.toDto(): MedicationScheduleDTO = MedicationScheduleDTO(
    id = id,
    time = time,
    dosage = dosage,
    mealTiming = mealTiming.name,
    basis = basis.name,
    mealSlot = mealSlot?.name,
    mealInterval = mealInterval.name
)

fun MedicationDTO.toDomain(): Medication = Medication(
    id = id,
    userId = userId,
    name = name,
    type = MedicationType.from(type),
    startDate = startDate?.toDate()?.time ?: 0L,
    endDate = endDate?.toDate()?.time,
    repeatType = RepeatType.from(repeatType),
    weeklyDays = weeklyDays.toSet(),
    isAlarmEnabled = isAlarmEnabled,
    status = MedicationStatus.from(status),
    memo = memo,
    schedules = schedules.map { it.toDomain() },
    prescriptionImageUrl = prescriptionImageUrl,
    intakeGroupId = intakeGroupId,
    isGrouped = isGrouped
)

fun Medication.toDto(): MedicationDTO = MedicationDTO(
    id = id,
    userId = userId,
    name = name,
    type = type.name,
    startDate = startDate.toTimestamp(),
    endDate = endDate?.toTimestamp(),
    repeatType = repeatType.name,
    weeklyDays = weeklyDays.sorted(),
    isAlarmEnabled = isAlarmEnabled,
    status = status.name,
    memo = memo,
    schedules = schedules.map { it.toDto() },
    prescriptionImageUrl = prescriptionImageUrl,
    intakeGroupId = intakeGroupId,
    isGrouped = isGrouped
)

private fun String.toScheduleBasis(): MedicationScheduleBasis =
    MedicationScheduleBasis.entries.find { it.name == this } ?: MedicationScheduleBasis.FIXED_TIME

private fun String.toMealInterval(): MealInterval =
    MealInterval.entries.find { it.name == this } ?: MealInterval.IMMEDIATELY
