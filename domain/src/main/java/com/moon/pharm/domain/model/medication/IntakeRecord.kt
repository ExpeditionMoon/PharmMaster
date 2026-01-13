package com.moon.pharm.domain.model.medication

data class IntakeRecord(
    val id: String,
    val userId: String,
    val medicationId: String,
    val scheduleId: String,
    val recordDate: String,
    val isTaken: Boolean,
    val takenTime: Long? = null
)