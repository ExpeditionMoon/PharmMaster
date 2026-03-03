package com.moon.pharm.data.datasource.remote.dto

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.moon.pharm.data.common.EMPTY_STRING
import com.moon.pharm.data.common.toTimestamp
import com.moon.pharm.domain.model.medication.IntakeRecord

@IgnoreExtraProperties
data class IntakeRecordDTO(
    @DocumentId
    val id: String = EMPTY_STRING,

    val userId: String = EMPTY_STRING,
    val medicationId: String = EMPTY_STRING,
    val scheduleId: String = EMPTY_STRING,
    val recordDate: String = EMPTY_STRING,

    @get:PropertyName("isTaken")
    val isTaken: Boolean = false,
    val takenTime: Timestamp? = null
)

// IntakeRecord 매퍼
fun IntakeRecordDTO.toDomain(): IntakeRecord = IntakeRecord(
    id = this.id,
    userId = this.userId,
    medicationId = this.medicationId,
    scheduleId = this.scheduleId,
    recordDate = this.recordDate,
    isTaken = this.isTaken,
    takenTime = this.takenTime?.toDate()?.time
)

fun IntakeRecord.toDto(): IntakeRecordDTO = IntakeRecordDTO(
    userId = this.userId,
    medicationId = this.medicationId,
    scheduleId = this.scheduleId,
    recordDate = this.recordDate,
    isTaken = this.isTaken,
    takenTime = this.takenTime?.toTimestamp()
)