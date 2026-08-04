package com.moon.pharm.profile.medication.viewmodel

import com.moon.pharm.designsystem.common.UiMessage
import com.moon.pharm.domain.usecase.medication.MedicationStatusInput
import com.moon.pharm.profile.medication.model.MealTimingUiModel
import com.moon.pharm.profile.medication.model.MedicationPrimaryTab
import com.moon.pharm.profile.medication.model.MedicationTypeUiModel
import com.moon.pharm.profile.medication.model.RepeatTypeUiModel
import com.moon.pharm.profile.medication.model.TodayMedicationUiModel

data class MedicationUiState(
    val isLoading: Boolean = false,
    val userMessage: UiMessage? = null,
    val isMedicationCreated: Boolean = false,
    val isEditing: Boolean = false,
    val isPrescriptionReview: Boolean = false,

    val medicationList: List<TodayMedicationUiModel> = emptyList(),
    val weeklyTotalCount: Int = 0,
    val weeklyCompletedCount: Int = 0,
    val selectedTab: MedicationPrimaryTab = MedicationPrimaryTab.ALL,
    val sharedMedicationDosage: String = "",
    val isIndividualDosageEditorVisible: Boolean = false,
    val medicationForms: List<MedicationFormState> = listOf(MedicationFormState())
)

data class MedicationFormState(
    val medicationId: String? = null,
    val scheduleId: String? = null,
    val medicationName: String = "",
    val dailyCount: Int = 0,
    val medicationDosage: String? = "",
    val usesIndividualDosage: Boolean = false,
    val selectedType: MedicationTypeUiModel = MedicationTypeUiModel.Prescription,
    val startDate: Long? = null,
    val endDate: Long? = null,
    val noEndDate: Boolean = false,
    val selectedMealTiming: MealTimingUiModel = MealTimingUiModel.BeforeMeal,
    val selectedTime: Long? = null,
    val selectedRepeatType: RepeatTypeUiModel = RepeatTypeUiModel.Daily,
    val selectedWeeklyDays: Set<Int> = emptySet(),
    val isGrouped: Boolean = false,
    val isAlarmEnabled: Boolean = true,
    val status: MedicationStatusInput = MedicationStatusInput.ACTIVE
)
