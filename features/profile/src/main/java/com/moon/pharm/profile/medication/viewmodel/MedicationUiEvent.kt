package com.moon.pharm.profile.medication.viewmodel

import com.moon.pharm.profile.medication.model.MealTimingUiModel
import com.moon.pharm.profile.medication.model.MedicationPrimaryTab
import com.moon.pharm.profile.medication.model.MedicationTypeUiModel
import com.moon.pharm.profile.medication.model.RepeatTypeUiModel

sealed interface MedicationUiEvent {
    // 1. 폼 입력 이벤트 (Form Inputs)
    data class UpdateType(val index: Int = 0, val type: MedicationTypeUiModel) : MedicationUiEvent
    data class UpdateName(val index: Int = 0, val name: String) : MedicationUiEvent
    data class UpdateDosage(val index: Int = 0, val dosage: String) : MedicationUiEvent
    data class ToggleIndividualDosage(val index: Int) : MedicationUiEvent
    data object ToggleIndividualDosageEditor : MedicationUiEvent
    data class UpdateStartDate(val index: Int = 0, val millis: Long?) : MedicationUiEvent
    data class UpdateEndDate(val index: Int = 0, val millis: Long?) : MedicationUiEvent
    data class UpdatePeriod(val index: Int = 0, val start: Long?, val end: Long?, val noEnd: Boolean) : MedicationUiEvent
    data class UpdateMealTiming(val index: Int = 0, val timing: MealTimingUiModel) : MedicationUiEvent
    data class UpdateAlarmTime(val index: Int = 0, val hour: Int, val minute: Int) : MedicationUiEvent
    data class UpdateRepeatType(val index: Int = 0, val type: RepeatTypeUiModel) : MedicationUiEvent
    data class ToggleWeeklyDay(val index: Int = 0, val day: Int) : MedicationUiEvent
    data class UpdateGroupedNotification(val index: Int = 0, val enabled: Boolean) : MedicationUiEvent
    data class UpdateAlarmEnabled(val index: Int = 0, val enabled: Boolean) : MedicationUiEvent
    data object AddMedication : MedicationUiEvent
    data class RemoveMedication(val index: Int) : MedicationUiEvent

    // 2. 주요 비즈니스 로직 (Business Logic)
    object SaveAllMedications : MedicationUiEvent
    data class ToggleTaken(val medicationId: String, val scheduleId: String) : MedicationUiEvent
    data class PauseMedication(val medicationId: String) : MedicationUiEvent
    data class ResumeMedication(val medicationId: String) : MedicationUiEvent
    data class EndMedication(val medicationId: String) : MedicationUiEvent
    data class DeleteMedication(val medicationId: String) : MedicationUiEvent

    // 3. UI 상태 및 시스템 이벤트 (UI State & System)
    data class SelectTab(val tab: MedicationPrimaryTab) : MedicationUiEvent
    object MessageShown : MedicationUiEvent
}
