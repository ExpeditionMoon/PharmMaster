package com.moon.pharm.profile.medication.model

data class HistoryRecordUiModel(
    val recordId: String,
    val medicationId: String,
    val scheduleId: String,
    val recordDate: String,
    val isTaken: Boolean,
    val takenTime: Long? = null,
    val medicationName: String,
    val time: String
)
