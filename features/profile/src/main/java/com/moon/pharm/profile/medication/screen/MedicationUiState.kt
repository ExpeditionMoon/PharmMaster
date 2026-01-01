package com.moon.pharm.profile.medication.screen

import com.moon.pharm.domain.model.MealTiming
import com.moon.pharm.domain.model.MedicationItem
import com.moon.pharm.domain.model.MedicationType
import com.moon.pharm.domain.model.RepeatType
import com.moon.pharm.profile.medication.model.MedicationPrimaryTab
import java.time.LocalDate
import java.time.LocalTime

data class MedicationUiState(
    val isLoading: Boolean = false,
    val userMessage: String? = null,
    val isMedicationCreated: Boolean = false,

    val medicationList: List<MedicationItem> = emptyList(),
    val selectedTab: MedicationPrimaryTab = MedicationPrimaryTab.ALL,

    val form: MedicationForm = MedicationForm()
)

data class MedicationForm(
    val medicationName: String = "",
    val medicationDosage: String? = "",
    val selectedType: MedicationType = MedicationType.PRESCRIPTION,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val noEndDate: Boolean = false,
    val selectedMealTiming: MealTiming = MealTiming.BEFORE_MEAL,
    val selectedTime: LocalTime? = null,
    val selectedRepeatType: RepeatType = RepeatType.DAILY,
    val isMealTimeAlarmEnabled: Boolean = false
)