package com.moon.pharm.data.datasource.remote.dto

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.moon.pharm.data.common.EMPTY_STRING
import com.moon.pharm.data.common.toTimestamp
import com.moon.pharm.domain.model.medication.MealTiming
import com.moon.pharm.domain.model.medication.Medication
import com.moon.pharm.domain.model.medication.MedicationSchedule
import com.moon.pharm.domain.model.medication.MedicationType
import com.moon.pharm.domain.model.medication.RepeatType

@IgnoreExtraProperties
data class MedicationDTO(
    @DocumentId
    val id: String = EMPTY_STRING,

    val userId: String = EMPTY_STRING,
    val name: String = EMPTY_STRING,
    val type: String = EMPTY_STRING,
    val startDate: Timestamp? = null,
    val endDate: Timestamp? = null,
    val repeatType: String = EMPTY_STRING,
    val memo: String? = null,

    val schedules: List<MedicationScheduleDTO> = emptyList(),
    val prescriptionImageUrl: String? = null,
    val isGrouped: Boolean = false
)

@IgnoreExtraProperties
data class MedicationScheduleDTO(
    var id: String = EMPTY_STRING,
    var time: String = EMPTY_STRING,
    var dosage: String = EMPTY_STRING,
    var mealTiming: String = EMPTY_STRING
)

// Schedule 매퍼
fun MedicationScheduleDTO.toDomain(): MedicationSchedule = MedicationSchedule(
    id = this.id,
    time = this.time,
    dosage = this.dosage,
    mealTiming = MealTiming.from(this.mealTiming)
)

fun MedicationSchedule.toDto(): MedicationScheduleDTO = MedicationScheduleDTO(
    id = this.id,
    time = this.time,
    dosage = this.dosage,
    mealTiming = this.mealTiming.name
)

// Medication 매퍼
fun MedicationDTO.toDomain(): Medication = Medication(
    id = this.id,
    userId = this.userId,
    name = this.name,
    type = MedicationType.from(this.type),
    startDate = this.startDate?.toDate()?.time ?: 0L,
    endDate = this.endDate?.toDate()?.time,
    repeatType = RepeatType.from(this.repeatType),
    memo = this.memo,
    schedules = this.schedules.map { it.toDomain() },
    prescriptionImageUrl = this.prescriptionImageUrl,
    isGrouped = this.isGrouped
)

fun Medication.toDto(): MedicationDTO = MedicationDTO(
    id = this.id,
    userId = this.userId,
    name = this.name,
    type = this.type.name,
    startDate = this.startDate?.toTimestamp(),
    endDate = this.endDate?.toTimestamp(),
    repeatType = this.repeatType.name,
    memo = this.memo,
    schedules = this.schedules.map { it.toDto() },
    prescriptionImageUrl = this.prescriptionImageUrl,
    isGrouped = this.isGrouped
)

fun List<MedicationDTO>.toDomainList() = this.map { it.toDomain() }