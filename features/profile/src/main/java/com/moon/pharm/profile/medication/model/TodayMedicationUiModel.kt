package com.moon.pharm.profile.medication.model

data class TodayMedicationUiModel(
    val medicationId: String,
    val scheduleId: String,
    val name: String,
    val type: MedicationTypeUiModel,
    val repeatType: RepeatTypeUiModel,
    val time: String,
    val dosage: String,
    val mealTiming: MealTimingUiModel,
    val isTaken: Boolean
)
