package com.moon.pharm.data.mapper

import com.moon.pharm.data.common.toTimestamp
import com.moon.pharm.data.datasource.remote.dto.IntakeRecordDTO
import com.moon.pharm.domain.model.medication.IntakeRecord

fun IntakeRecordDTO.toDomain(): IntakeRecord = IntakeRecord(
    id = id,
    userId = userId,
    medicationId = medicationId,
    scheduleId = scheduleId,
    recordDate = recordDate,
    isTaken = isTaken,
    takenTime = takenTime?.toDate()?.time
)

fun IntakeRecord.toDto(): IntakeRecordDTO = IntakeRecordDTO(
    userId = userId,
    medicationId = medicationId,
    scheduleId = scheduleId,
    recordDate = recordDate,
    isTaken = isTaken,
    takenTime = takenTime?.toTimestamp()
)
