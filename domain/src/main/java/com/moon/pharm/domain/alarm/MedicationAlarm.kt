package com.moon.pharm.domain.alarm

import com.moon.pharm.domain.model.medication.RepeatType

data class MedicationAlarm(
    val requestCode: Int,
    val medicationId: String,
    val name: String,
    val dosage: String,
    val time: String,
    val isGrouped: Boolean,
    val repeatType: RepeatType,
    val weeklyDays: Set<Int>,
    val startDate: Long?,
    val endDate: Long?
)
