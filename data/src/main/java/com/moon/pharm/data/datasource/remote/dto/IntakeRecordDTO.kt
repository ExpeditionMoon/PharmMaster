package com.moon.pharm.data.datasource.remote.dto

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.moon.pharm.data.common.EMPTY_STRING

@IgnoreExtraProperties
data class IntakeRecordDTO(
    @DocumentId
    val id: String = EMPTY_STRING,

    val userId: String = EMPTY_STRING,
    val medicationId: String = EMPTY_STRING,
    val scheduleId: String = EMPTY_STRING,
    val recordDate: String = EMPTY_STRING,
    val isTaken: Boolean = false,
    val takenTime: Timestamp? = null
)