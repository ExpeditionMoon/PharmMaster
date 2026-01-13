package com.moon.pharm.profile.medication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.component_ui.util.toDisplayDateString
import com.moon.pharm.domain.model.medication.MealTiming
import com.moon.pharm.domain.model.medication.MedicationProgress
import com.moon.pharm.domain.model.medication.MedicationTimeGroup
import com.moon.pharm.domain.model.medication.MedicationType
import com.moon.pharm.domain.model.medication.RepeatType
import com.moon.pharm.domain.model.medication.TodayMedicationUiModel
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.medication.MedicationUseCases
import com.moon.pharm.profile.medication.common.MedicationUiMessage
import com.moon.pharm.profile.medication.common.toMedication
import com.moon.pharm.profile.medication.model.MedicationPrimaryTab
import com.moon.pharm.profile.medication.screen.MedicationUiState
import com.moon.pharm.profile.medication.screen.MedicationFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MedicationUiEvent {
    data class UpdateType(val type: MedicationType) : MedicationUiEvent
    data class UpdateName(val name: String) : MedicationUiEvent
    data class UpdateDosage(val dosage: String) : MedicationUiEvent
    data class UpdateStartDate(val millis: Long?) : MedicationUiEvent
    data class UpdateEndDate(val millis: Long?) : MedicationUiEvent
    data class UpdatePeriod(val start: Long?, val end: Long?, val noEnd: Boolean) : MedicationUiEvent
    data class UpdateMealTiming(val timing: MealTiming) : MedicationUiEvent
    data class UpdateAlarmTime(val hour: Int, val minute: Int) : MedicationUiEvent
    data class UpdateRepeatType(val type: RepeatType) : MedicationUiEvent
    data class UpdateMealAlarm(val enabled: Boolean) : MedicationUiEvent
    object SaveMedication : MedicationUiEvent
    object ResetNavigation : MedicationUiEvent

    data class ToggleTaken(val uniqueId: String) : MedicationUiEvent
}

@HiltViewModel
class MedicationViewModel @Inject constructor(
    private val useCases: MedicationUseCases,
): ViewModel() {

    private val _uiState = MutableStateFlow(MedicationUiState())
    val uiState = _uiState.asStateFlow()

    val groupedMedications: StateFlow<List<MedicationTimeGroup>> = uiState
        .map { state ->
            state.medicationList
                .groupBy { it.time }
                .map { (time, items) -> MedicationTimeGroup(time = time, items = items) }
                .sortedBy { it.time }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val progress:  StateFlow<MedicationProgress> =
        uiState.map { state ->
            val total = state.medicationList.size
            val completed = state.medicationList.count { it.isTaken }

            if (total == 0) {
                MedicationProgress(0f, 0, 0)
            } else {
                MedicationProgress(
                    ratio = completed.toFloat() / total,
                    completed = completed,
                    total = total
                )
            }
    }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            MedicationProgress(0f, 0, 0)
    )

    init {
        fetchMedicationList()
    }

    fun onEvent(event: MedicationUiEvent) {
        when (event) {
            is MedicationUiEvent.UpdateType -> updateForm { it.copy(selectedType = event.type) }
            is MedicationUiEvent.UpdateName -> updateForm { it.copy(medicationName = event.name) }
            is MedicationUiEvent.UpdateDosage -> updateForm { it.copy(medicationDosage = event.dosage) }
            is MedicationUiEvent.UpdateStartDate -> updateForm { it.copy(startDate = event.millis) }
            is MedicationUiEvent.UpdateEndDate -> updateForm { it.copy(endDate = event.millis) }
            is MedicationUiEvent.UpdatePeriod -> updateForm { it.copy(startDate = event.start, endDate = event.end, noEndDate = event.noEnd) }
            is MedicationUiEvent.UpdateMealTiming -> updateForm { it.copy(selectedMealTiming = event.timing) }
            is MedicationUiEvent.UpdateAlarmTime -> updateForm { it.copy(selectedTime = (event.hour * 60 + event.minute).toLong()) }
            is MedicationUiEvent.UpdateRepeatType -> updateForm { it.copy(selectedRepeatType = event.type) }
            is MedicationUiEvent.UpdateMealAlarm -> updateForm { it.copy(isMealTimeAlarmEnabled = event.enabled) }

            MedicationUiEvent.SaveMedication -> saveMedication()
            is MedicationUiEvent.ResetNavigation -> _uiState.update { it.copy(isMedicationCreated = false) }

            is MedicationUiEvent.ToggleTaken -> toggleMedicationTaken(event.uniqueId)
        }
    }

    private fun updateForm(block: (MedicationFormState) -> MedicationFormState) {
        _uiState.update { it.copy(form = block(it.form)) }
    }

    fun onTabSelected(tab: MedicationPrimaryTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    private fun saveMedication() {
        val form = _uiState.value.form
        if (form.medicationName.isBlank()) {
            _uiState.update { it.copy(userMessage = MedicationUiMessage.EmptyMedicationName) }
            return
        }

        val userId = useCases.getCurrentUserId() ?: run {
            _uiState.update { it.copy(userMessage = UiMessage.Error("로그인이 필요합니다.")) }
            return
        }

        val newItem = form.toMedication(userId = userId)

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            useCases.saveMedication(newItem).collectLatest { result ->
                _uiState.update { currentState ->
                    when (result) {
                        is DataResourceResult.Loading -> currentState.copy(isLoading = true)
                        is DataResourceResult.Success -> currentState.copy(
                            isLoading = false,
                            isMedicationCreated = true
                        )
                        is DataResourceResult.Failure -> currentState.copy(
                            isLoading = false,
                            userMessage = MedicationUiMessage.CreateFailed
                        )
                        else -> currentState
                    }
                }
            }
        }
    }

    private fun fetchMedicationList() {
        val currentUserId = useCases.getCurrentUserId() ?: return
        val todayDate = System.currentTimeMillis().toDisplayDateString()

        viewModelScope.launch {
            useCases.getMedications(userId = currentUserId).collectLatest { result ->
                _uiState.update { currentState ->
                    when (result) {
                        is DataResourceResult.Loading -> {
                            currentState.copy(isLoading = true, userMessage = null)
                        }

                        is DataResourceResult.Success -> {
                            val uiModelList = result.resultData.flatMap { medication ->
                                medication.schedules.map { schedule ->
                                    TodayMedicationUiModel(
                                        medicationId = medication.id,
                                        scheduleId = schedule.id,
                                        name = medication.name,
                                        type = medication.type,
                                        repeatType = medication.repeatType,
                                        time = schedule.time,
                                        dosage = schedule.dosage,
                                        mealTiming = schedule.mealTiming,
                                        isTaken = false
                                    )
                                }
                            }

                            currentState.copy(
                                isLoading = false,
                                medicationList = uiModelList.sortedBy { it.time }
                            )
                        }

                        is DataResourceResult.Failure -> {
                            currentState.copy(
                                isLoading = false,
                                userMessage = result.exception.message
                                    ?.let { UiMessage.Error(it) }
                                    ?: UiMessage.LoadDataFailed
                            )
                        }
                    }
                }
            }
        }
    }

    private fun toggleMedicationTaken(id: String) {
        _uiState.update { state ->
            val newList = state.medicationList.map { item ->
                if (item.medicationId  == id) item.copy(isTaken = !item.isTaken) else item
            }
            state.copy(medicationList = newList)
        }
    }
}