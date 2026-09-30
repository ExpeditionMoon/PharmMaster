package com.moon.pharm.profile.medication.model

data class TodayMedicationUiModel(
    val medicationId: String,
    val intakeGroupId: String? = null,
    val scheduleId: String,
    val name: String,
    val type: MedicationTypeUiModel,
    val repeatType: RepeatTypeUiModel,
    val time: String,
    val dosage: String,
    val mealTiming: MealTimingUiModel,
    val isPaused: Boolean,
    val isTaken: Boolean
)
