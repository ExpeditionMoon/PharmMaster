package com.moon.pharm.profile.medication.common

import com.moon.pharm.domain.model.MedicationItem
import com.moon.pharm.profile.medication.screen.MedicationForm

fun MedicationForm.toMedicationItem(): MedicationItem {
    return MedicationItem(
        id = "",
        name = medicationName,
        dosage = medicationDosage,
        type = selectedType,
        startDate = startDate,
        endDate = endDate,
        noEndDate = noEndDate,
        alarmTime = selectedTime,
        mealTiming = selectedMealTiming,
        repeatType = selectedRepeatType
    )
}