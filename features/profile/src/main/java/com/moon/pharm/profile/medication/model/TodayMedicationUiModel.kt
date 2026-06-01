package com.moon.pharm.profile.medication.model

import com.moon.pharm.domain.model.medication.MealTiming
import com.moon.pharm.domain.model.medication.MedicationType
import com.moon.pharm.domain.model.medication.RepeatType

data class TodayMedicationUiModel(
    val medicationId: String,
    val scheduleId: String,
    val name: String,
    val type: MedicationType,
    val repeatType: RepeatType,
    val time: String,
    val dosage: String,
    val mealTiming: MealTiming,
    val isTaken: Boolean
)
