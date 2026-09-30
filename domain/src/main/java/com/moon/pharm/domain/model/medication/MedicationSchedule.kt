package com.moon.pharm.domain.model.medication

data class MedicationSchedule(
    val id: String,
    val time: String,
    val dosage: String,
    val mealTiming: MealTiming,
    val basis: MedicationScheduleBasis = MedicationScheduleBasis.FIXED_TIME,
    val mealSlot: MealSlot? = null,
    val mealInterval: MealInterval = MealInterval.IMMEDIATELY
)
