package com.moon.pharm.profile.medication.model

import com.moon.pharm.domain.model.medication.IntakeRecord

data class HistoryRecordUiModel(
    val record: IntakeRecord,
    val medicationName: String,
    val time: String
)