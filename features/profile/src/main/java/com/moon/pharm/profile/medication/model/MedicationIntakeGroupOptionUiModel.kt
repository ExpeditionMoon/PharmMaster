package com.moon.pharm.profile.medication.model

data class MedicationIntakeGroupOptionUiModel(
    val id: String,
    val representativeName: String,
    val time: Long?,
    val mealTiming: MealTimingUiModel,
    val scheduleBasis: MedicationScheduleBasisUiModel,
    val mealSlots: Set<MealSlotUiModel>,
    val mealInterval: MealIntervalUiModel,
    val repeatType: RepeatTypeUiModel,
    val weeklyDays: Set<Int>
)
