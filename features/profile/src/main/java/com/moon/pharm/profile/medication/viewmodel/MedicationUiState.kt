package com.moon.pharm.profile.medication.viewmodel

import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.domain.model.medication.MealTiming
import com.moon.pharm.domain.model.medication.MedicationType
import com.moon.pharm.domain.model.medication.RepeatType
import com.moon.pharm.domain.model.medication.TodayMedicationUiModel
import com.moon.pharm.profile.medication.model.MedicationPrimaryTab

data class MedicationUiState(
    val isLoading: Boolean = false,
    val userMessage: UiMessage? = null,
    val isMedicationCreated: Boolean = false,

    val medicationList: List<TodayMedicationUiModel> = emptyList(),
    val selectedTab: MedicationPrimaryTab = MedicationPrimaryTab.ALL,
    val form: MedicationFormState = MedicationFormState()
)

data class MedicationFormState(
    val medicationName: String = "",
    val medicationDosage: String? = "",
    val selectedType: MedicationType = MedicationType.PRESCRIPTION,
    val startDate: Long? = null,
    val endDate: Long? = null,
    val noEndDate: Boolean = false,
    val selectedMealTiming: MealTiming = MealTiming.BEFORE_MEAL,
    val selectedTime: Long? = null,
    val selectedRepeatType: RepeatType = RepeatType.DAILY,
    val isGrouped: Boolean = false
)