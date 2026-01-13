package com.moon.pharm.domain.model.medication

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