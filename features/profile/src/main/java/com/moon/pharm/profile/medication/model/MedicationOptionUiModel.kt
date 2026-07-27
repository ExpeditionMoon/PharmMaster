package com.moon.pharm.profile.medication.model

import androidx.annotation.StringRes
import com.moon.pharm.profile.R

enum class MedicationTypeUiModel(@param:StringRes val labelRes: Int) {
    Prescription(R.string.medication_category_prescription),
    Otc(R.string.medication_category_general),
    Supplement(R.string.medication_category_supplements)
}

enum class MealTimingUiModel(@param:StringRes val labelRes: Int) {
    BeforeMeal(R.string.medication_meal_before),
    DuringMeal(R.string.medication_meal_during),
    AfterMeal(R.string.medication_meal_after),
    None(R.string.medication_meal_none)
}

enum class RepeatTypeUiModel(@param:StringRes val labelRes: Int) {
    Daily(R.string.medication_repeat_daily),
    Weekly(R.string.medication_repeat_weekly),
    Period(R.string.medication_repeat_period)
}
