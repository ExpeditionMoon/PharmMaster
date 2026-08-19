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

enum class MedicationScheduleBasisUiModel(@param:StringRes val labelRes: Int) {
    FixedTime(R.string.medication_schedule_fixed_time),
    BeforeMeal(R.string.medication_schedule_before_meal),
    AfterMeal(R.string.medication_schedule_after_meal),
    AsNeeded(R.string.medication_schedule_as_needed)
}

enum class MealSlotUiModel(@param:StringRes val labelRes: Int) {
    Breakfast(R.string.medication_meal_breakfast),
    Lunch(R.string.medication_meal_lunch),
    Dinner(R.string.medication_meal_dinner)
}

enum class MealIntervalUiModel(@param:StringRes val labelRes: Int) {
    Immediately(R.string.medication_interval_immediately),
    ThirtyMinutes(R.string.medication_interval_thirty_minutes),
    OneHour(R.string.medication_interval_one_hour)
}

enum class RepeatTypeUiModel(@param:StringRes val labelRes: Int) {
    Daily(R.string.medication_repeat_daily),
    Weekly(R.string.medication_repeat_weekly),
    Period(R.string.medication_repeat_period)
}

enum class WeekdayUiModel(val value: Int, @param:StringRes val labelRes: Int) {
    Monday(1, R.string.medication_weekday_mon),
    Tuesday(2, R.string.medication_weekday_tue),
    Wednesday(3, R.string.medication_weekday_wed),
    Thursday(4, R.string.medication_weekday_thu),
    Friday(5, R.string.medication_weekday_fri),
    Saturday(6, R.string.medication_weekday_sat),
    Sunday(7, R.string.medication_weekday_sun)
}
