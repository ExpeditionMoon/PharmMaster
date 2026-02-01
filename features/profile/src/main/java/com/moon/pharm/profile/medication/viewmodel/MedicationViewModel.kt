package com.moon.pharm.profile.medication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.domain.alarm.AlarmScheduler
import com.moon.pharm.domain.model.medication.MedicationProgress
import com.moon.pharm.domain.model.medication.MedicationTimeGroup
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.auth.GetCurrentUserIdUseCase
import com.moon.pharm.domain.usecase.medication.GetMedicationsUseCase
import com.moon.pharm.domain.usecase.medication.SaveMedicationUseCase
import com.moon.pharm.domain.usecase.medication.ToggleIntakeCheckUseCase
import com.moon.pharm.domain.usecase.medication.ValidateMedicationEntryUseCase
import com.moon.pharm.profile.medication.mapper.MedicationUiMapper
import com.moon.pharm.profile.medication.mapper.toUiMessage
import com.moon.pharm.profile.medication.model.MedicationPrimaryTab
import com.moon.pharm.profile.medication.model.MedicationUiMessage
import com.moon.pharm.profile.medication.screen.MedicationFormState
import com.moon.pharm.profile.medication.screen.MedicationUiState
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

@HiltViewModel
class MedicationViewModel @Inject constructor(
    private val getMedicationsUseCase: GetMedicationsUseCase,
    private val saveMedicationUseCase: SaveMedicationUseCase,
    private val toggleIntakeCheckUseCase: ToggleIntakeCheckUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val validateMedicationEntryUseCase: ValidateMedicationEntryUseCase,
    private val alarmScheduler: AlarmScheduler
): ViewModel() {

    // region 1. State & Derived State
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

    val progress: StateFlow<MedicationProgress> = uiState
        .map { calculateProgress(it.medicationList) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MedicationProgress(0f, 0, 0))
    // endregion

    init {
        fetchMedicationList()
    }

    // region 2. Event Handler
    fun onEvent(event: MedicationUiEvent) {
        when (event) {
            // 1. 폼 입력 이벤트 (Form Inputs)
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

            // 2. 주요 비즈니스 로직 (Business Logic)
            MedicationUiEvent.SaveMedication -> saveMedication()
            is MedicationUiEvent.ToggleTaken -> toggleMedicationTaken(event.uniqueId)

            // 3. UI 상태 및 시스템 이벤트 (UI State & System)
            is MedicationUiEvent.SelectTab -> _uiState.update { it.copy(selectedTab = event.tab) }
            MedicationUiEvent.ResetNavigation -> _uiState.update { it.copy(isMedicationCreated = false) }
            MedicationUiEvent.MessageShown -> _uiState.update { it.copy(userMessage = null) }
        }
    }

    fun onTabSelected(tab: MedicationPrimaryTab) { _uiState.update { it.copy(selectedTab = tab) } }
    // endregion

    // region 3. Actions
    private fun fetchMedicationList() {
        val userId = getCurrentUserIdUseCase() ?: return

        viewModelScope.launch {
            getMedicationsUseCase(userId = userId).collectLatest { result ->
                _uiState.update { currentState ->
                    when (result) {
                        is DataResourceResult.Loading -> { currentState.copy(isLoading = true, userMessage = null) }
                        is DataResourceResult.Success -> {
                            val uiModelList = MedicationUiMapper.toUiModelList(result.resultData)
                            currentState.copy(isLoading = false, medicationList = uiModelList)
                        }
                        is DataResourceResult.Failure -> {
                            currentState.copy(
                                isLoading = false,
                                userMessage = result.exception.message?.let { UiMessage.Error(it) } ?: UiMessage.LoadDataFailed
                            )
                        }
                    }
                }
            }
        }
    }

    private fun saveMedication() {
        val form = _uiState.value.form

        val validationResult = validateMedicationEntryUseCase(form.medicationName)
        if (validationResult is ValidateMedicationEntryUseCase.Result.Error) {
            val uiMessage = validationResult.error.toUiMessage()
            _uiState.update { it.copy(userMessage = uiMessage) }
            return
        }

        val userId = getCurrentUserIdUseCase()
        if (userId == null) {
            _uiState.update { it.copy(userMessage = MedicationUiMessage.NotLoggedIn) }
            return
        }

        val newItem = MedicationUiMapper.toDomain(form, userId)

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            saveMedicationUseCase(newItem).collectLatest { result ->
                if (result is DataResourceResult.Success) {
                    alarmScheduler.schedule(newItem)
                }

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
                    }
                }
            }
        }
    }

    private fun toggleMedicationTaken(medicationId: String) {
        val userId = getCurrentUserIdUseCase() ?: return

        _uiState.update { state ->
            val newList = state.medicationList.map { item ->
                if (item.medicationId == medicationId) item.copy(isTaken = !item.isTaken) else item
            }
            state.copy(medicationList = newList)
        }

        viewModelScope.launch {
            val updatedItem = _uiState.value.medicationList.find { it.medicationId == medicationId } ?: return@launch

            val record = MedicationUiMapper.toIntakeRecord(updatedItem, userId)
            toggleIntakeCheckUseCase(
                record = record,
                isCurrentlyChecked = !updatedItem.isTaken
            ).collectLatest { result ->
                if (result is DataResourceResult.Failure) {
                    result.exception.printStackTrace()
                }
            }
        }
    }
    // endregion

    // region 4. Helper Functions
    private fun updateForm(block: (MedicationFormState) -> MedicationFormState) {
        _uiState.update { it.copy(form = block(it.form)) }
    }

    private fun calculateProgress(list: List<com.moon.pharm.domain.model.medication.TodayMedicationUiModel>): MedicationProgress {
        val total = list.size
        val completed = list.count { it.isTaken }
        if (total == 0) return MedicationProgress(0f, 0, 0)
        return MedicationProgress(
            ratio = completed.toFloat() / total,
            completed = completed,
            total = total
        )
    }
    // endregion
}