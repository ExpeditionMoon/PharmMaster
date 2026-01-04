package com.moon.pharm.profile.medication.screen

import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.domain.model.MealTiming
import com.moon.pharm.domain.model.MedicationItem
import com.moon.pharm.domain.model.MedicationType
import com.moon.pharm.domain.model.RepeatType
import com.moon.pharm.profile.medication.model.MedicationPrimaryTab

data class MedicationUiState(
    val isLoading: Boolean = false,
    val userMessage: UiMessage? = null,
    val isMedicationCreated: Boolean = false,

    val medicationList: List<MedicationItem> = emptyList(),
    val selectedTab: MedicationPrimaryTab = MedicationPrimaryTab.ALL,

    val form: MedicationForm = MedicationForm()
)

data class MedicationForm(
    val medicationName: String = "",
    val medicationDosage: String? = "",
    val selectedType: MedicationType = MedicationType.PRESCRIPTION,
    val startDate: Long? = null,
    val endDate: Long? = null,
    val noEndDate: Boolean = false,
    val selectedMealTiming: MealTiming = MealTiming.BEFORE_MEAL,
    val selectedTime: Long? = null,
    val selectedRepeatType: RepeatType = RepeatType.DAILY,
    val isMealTimeAlarmEnabled: Boolean = false
)