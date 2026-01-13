package com.moon.pharm.data.mapper

import com.moon.pharm.data.common.toTimestamp
import com.moon.pharm.data.datasource.remote.dto.IntakeRecordDTO
import com.moon.pharm.data.datasource.remote.dto.MedicationDTO
import com.moon.pharm.data.datasource.remote.dto.MedicationScheduleDTO
import com.moon.pharm.domain.model.medication.IntakeRecord
import com.moon.pharm.domain.model.medication.MealTiming
import com.moon.pharm.domain.model.medication.Medication
import com.moon.pharm.domain.model.medication.MedicationSchedule
import com.moon.pharm.domain.model.medication.MedicationType
import com.moon.pharm.domain.model.medication.RepeatType
import kotlin.collections.map

fun MedicationDTO.toDomain(): Medication {
    return Medication(
        id = this.id,
        userId = this.userId,
        name = this.name,
        type = try { MedicationType.valueOf(this.type) } catch(e: Exception) { MedicationType.OTC },
        startDate = this.startDate?.toDate()?.time ?: 0L,
        endDate = this.endDate?.toDate()?.time,
        repeatType = try { RepeatType.valueOf(this.repeatType) } catch(e: Exception) { RepeatType.DAILY },
        memo = this.memo,
        schedules = this.schedules.map { it.toDomain() }
    )
}
fun Medication.toDto(): MedicationDTO {
    return MedicationDTO(
        id = this.id,
        userId = this.userId,
        name = this.name,
        type = this.type.name,
        startDate = this.startDate.toTimestamp(),
        endDate = this.endDate?.toTimestamp(),
        repeatType = this.repeatType.name,
        memo = this.memo,
        schedules = this.schedules.map { it.toDto() }
    )
}

fun MedicationScheduleDTO.toDomain(): MedicationSchedule {
    return MedicationSchedule(
        id = this.id,
        time = this.time,
        dosage = this.dosage,
        mealTiming = try {
            MealTiming.valueOf(this.mealTiming)
        } catch (e: Exception) {
            MealTiming.NONE
        }
    )
}

fun MedicationSchedule.toDto(): MedicationScheduleDTO {
    return MedicationScheduleDTO(
        id = this.id,
        time = this.time,
        dosage = this.dosage,
        mealTiming = this.mealTiming.name
    )
}

fun IntakeRecordDTO.toDomain(): IntakeRecord {
    return IntakeRecord(
        id = this.id,
        userId = this.userId,
        medicationId = this.medicationId,
        scheduleId = this.scheduleId,
        recordDate = this.recordDate,
        isTaken = this.isTaken,
        takenTime = this.takenTime?.toDate()?.time
    )
}

fun IntakeRecord.toDto(): IntakeRecordDTO {
    return IntakeRecordDTO(
        userId = this.userId,
        medicationId = this.medicationId,
        scheduleId = this.scheduleId,
        recordDate = this.recordDate,
        isTaken = this.isTaken,
        takenTime = this.takenTime?.toTimestamp()
    )
}

fun List<MedicationDTO>.toDomainList() = this.map { it.toDomain() }
