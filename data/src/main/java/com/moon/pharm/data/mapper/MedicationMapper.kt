package com.moon.pharm.data.mapper

import com.moon.pharm.data.common.parseAlarmTimeToLong
import com.moon.pharm.data.common.toLong
import com.moon.pharm.data.common.toTimestamp
import com.moon.pharm.data.remote.dto.MedicationItemDTO
import com.moon.pharm.domain.model.MealTiming
import com.moon.pharm.domain.model.MedicationItem
import com.moon.pharm.domain.model.MedicationType
import com.moon.pharm.domain.model.RepeatType

fun MedicationItemDTO.toDomainMedication(): MedicationItem {
    return MedicationItem(
        id = this.id,
        name = this.name,
        dosage = this.dosage,
        type = MedicationType.valueOf(this.type),
        startDate = this.startDate.toLong(),
        endDate = this.endDate?.toLong(),
        noEndDate = this.noEndDate,
        alarmTime = parseAlarmTimeToLong(this.alarmTime),
        mealTiming = MealTiming.valueOf(this.mealTiming),
        repeatType = RepeatType.valueOf(this.repeatType),
        isTaken = this.isTaken
    )
}

fun MedicationItem.toFirestoreMedicationDTO(): MedicationItemDTO {
    return MedicationItemDTO(
        id = this.id,
        name = this.name,
        dosage = this.dosage,
        type = this.type.name,
        startDate = this.startDate.toTimestamp(),
        endDate = this.endDate.toTimestamp(),
        noEndDate = this.noEndDate,
        alarmTime = this.alarmTime?.toString() ?: "",
        mealTiming = this.mealTiming.name,
        repeatType = this.repeatType.name,
        isTaken = this.isTaken
    )
}

fun List<MedicationItemDTO>.toDomainMedicationList() =
    this.map { it.toDomainMedication() }.toList()
