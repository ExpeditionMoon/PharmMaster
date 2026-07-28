package com.moon.pharm.profile.medication.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.moon.pharm.designsystem.common.UiMessage
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.auth.GetCurrentUserIdUseCase
import com.moon.pharm.domain.usecase.medication.DeleteMedicationUseCase
import com.moon.pharm.domain.usecase.medication.ObserveTodayMedicationItemsUseCase
import com.moon.pharm.domain.usecase.medication.SaveMedicationUseCase
import com.moon.pharm.domain.usecase.medication.ToggleIntakeCheckUseCase
import com.moon.pharm.domain.usecase.medication.ValidateMedicationEntryUseCase
import com.moon.pharm.profile.medication.mapper.MedicationUiMapper
import com.moon.pharm.profile.medication.mapper.toUiMessage
import com.moon.pharm.profile.medication.model.MedicationPrimaryTab
import com.moon.pharm.profile.medication.model.MedicationProgressUiModel
import com.moon.pharm.profile.medication.model.MedicationTimeGroupUiModel
import com.moon.pharm.profile.medication.model.MedicationUiMessage
import com.moon.pharm.profile.medication.model.TodayMedicationUiModel
import com.moon.pharm.profile.navigation.MedicationCreateRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MedicationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val observeTodayMedicationItemsUseCase: ObserveTodayMedicationItemsUseCase,
    private val saveMedicationUseCase: SaveMedicationUseCase,
    private val deleteMedicationUseCase: DeleteMedicationUseCase,
    private val toggleIntakeCheckUseCase: ToggleIntakeCheckUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val validateMedicationEntryUseCase: ValidateMedicationEntryUseCase,
) : ViewModel() {

    // region 1. State & Derived State
    private val _uiState = MutableStateFlow(MedicationUiState())
    val uiState = _uiState.asStateFlow()

    private var isSaving = false

    val groupedMedications: StateFlow<List<MedicationTimeGroupUiModel>> = uiState
        .map { state ->
            state.medicationList
                .groupBy { it.time }
                .map { (time, items) -> MedicationTimeGroupUiModel(time = time, items = items) }
                .sortedBy { it.time }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val progress: StateFlow<MedicationProgressUiModel> = uiState
        .map { calculateProgress(it.medicationList) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MedicationProgressUiModel(0f, 0, 0))
    // endregion

    init {
        fetchMedicationList()
        initializeFormFromArgs()
    }

    // region 2. Event Handler
    fun onEvent(event: MedicationUiEvent) {
        when (event) {
            is MedicationUiEvent.UpdateType -> {
                if (event.index == -1) {
                    _uiState.update { state ->
                        val newForms = state.medicationForms.map {
                            it.copy(selectedType = event.type)
                        }
                        state.copy(medicationForms = newForms)
                    }
                } else {
                    updateForm(event.index) { it.copy(selectedType = event.type) }
                }
            }
            is MedicationUiEvent.UpdateName -> updateForm(event.index) { it.copy(medicationName = event.name) }
            is MedicationUiEvent.UpdateDosage -> {
                if (event.index == -1) {
                    _uiState.update { state ->
                        val newForms = state.medicationForms.map {
                            it.copy(medicationDosage = event.dosage)
                        }
                        state.copy(medicationForms = newForms)
                    }
                } else {
                    updateForm(event.index) { it.copy(medicationDosage = event.dosage) }
                }
            }
            is MedicationUiEvent.UpdateStartDate -> {
                if (event.index == -1) {
                    _uiState.update { state ->
                        val newForms = state.medicationForms.map { it.copy(startDate = event.millis) }
                        state.copy(medicationForms = newForms)
                    }
                } else {
                    updateForm(event.index) { it.copy(startDate = event.millis) }
                }
            }

            is MedicationUiEvent.UpdateEndDate -> {
                if (event.index == -1) {
                    _uiState.update { state ->
                        val newForms = state.medicationForms.map { it.copy(endDate = event.millis) }
                        state.copy(medicationForms = newForms)
                    }
                } else {
                    updateForm(event.index) { it.copy(endDate = event.millis) }
                }
            }
            is MedicationUiEvent.UpdatePeriod -> {
                if (event.index == -1) {
                    _uiState.update { state ->
                        val updatedForms = state.medicationForms.map { form ->
                            form.copy(startDate = event.start, endDate = event.end, noEndDate = event.noEnd)
                        }
                        state.copy(medicationForms = updatedForms)
                    }
                } else {
                    updateForm(event.index) {
                        it.copy(startDate = event.start, endDate = event.end, noEndDate = event.noEnd)
                    }
                }
            }
            is MedicationUiEvent.UpdateMealTiming -> {
                if (event.index == -1) {
                    _uiState.update { state ->
                        val newForms = state.medicationForms.map {
                            it.copy(selectedMealTiming = event.timing)
                        }
                        state.copy(medicationForms = newForms)
                    }
                } else {
                    updateForm(event.index) { it.copy(selectedMealTiming = event.timing) }
                }
            }

            is MedicationUiEvent.UpdateAlarmTime -> {
                val newTime = (event.hour * 60 + event.minute).toLong()

                if (event.index == -1) {
                    _uiState.update { state ->
                        val newForms = state.medicationForms.map {
                            it.copy(selectedTime = newTime)
                        }
                        state.copy(medicationForms = newForms)
                    }
                } else {
                    updateForm(event.index) { it.copy(selectedTime = newTime) }
                }
            }
            is MedicationUiEvent.UpdateRepeatType -> {
                if (event.index == -1) {
                    _uiState.update { state ->
                        val newForms = state.medicationForms.map {
                            it.copy(selectedRepeatType = event.type)
                        }
                        state.copy(medicationForms = newForms)
                    }
                } else {
                    updateForm(event.index) { it.copy(selectedRepeatType = event.type) }
                }
            }
            is MedicationUiEvent.UpdateGroupedNotification -> {
                if (event.index == -1) {
                    _uiState.update { state ->
                        val newForms = state.medicationForms.map {
                            it.copy(isGrouped = event.enabled)
                        }
                        state.copy(medicationForms = newForms)
                    }
                } else {
                    updateForm(event.index) { it.copy(isGrouped = event.enabled) }
                }
            }
            is MedicationUiEvent.RemoveMedication -> {
                _uiState.update { state ->
                    val newForms = state.medicationForms.toMutableList().apply {
                        if (event.index in indices) { removeAt(event.index) }
                    }
                    if (newForms.isEmpty()) { newForms.add(MedicationFormState()) }
                    state.copy(medicationForms = newForms)
                }
            }

            // 2. 주요 비즈니스 로직
            MedicationUiEvent.SaveAllMedications -> saveAllMedications()
            is MedicationUiEvent.DeleteMedication -> deleteMedication(event.medicationId)
            is MedicationUiEvent.ToggleTaken -> toggleMedicationTaken(
                medicationId = event.medicationId,
                scheduleId = event.scheduleId
            )

            // 3. UI 상태 및 시스템 이벤트
            is MedicationUiEvent.SelectTab -> _uiState.update { it.copy(selectedTab = event.tab) }
            MedicationUiEvent.MessageShown -> _uiState.update { it.copy(userMessage = null) }
        }
    }

    fun onTabSelected(tab: MedicationPrimaryTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }
    // endregion

    // region 3. Actions
    private fun fetchMedicationList() {
        val userId = getCurrentUserIdUseCase() ?: return
        val today = LocalDate.now()

        viewModelScope.launch {
            observeTodayMedicationItemsUseCase(userId, today).collectLatest { result ->
                if (isSaving) return@collectLatest

                _uiState.update { currentState ->
                    when (result) {
                        is DataResourceResult.Loading -> currentState.copy(
                            isLoading = true,
                            userMessage = null
                        )
                        is DataResourceResult.Success -> {
                            currentState.copy(
                                isLoading = if (isSaving) true else false,
                                medicationList = MedicationUiMapper.toUiModelList(result.resultData)
                            )
                        }
                        is DataResourceResult.Failure -> {
                            currentState.copy(
                                isLoading = false,
                                userMessage = UiMessage.LoadDataFailed
                            )
                        }
                    }
                }
            }
        }
    }

    private fun initializeFormFromArgs() {
        val newForms = runCatching {
            savedStateHandle.toRoute<MedicationCreateRoute>()
        }.getOrNull()
            ?.scannedMedicationNames
            ?.takeIf { it.isNotEmpty() }
            ?.map {
                MedicationFormState(
                    medicationName = it,
                    dailyCount = 1
                )
            }
            ?: listOf(MedicationFormState())

        _uiState.update { it.copy(medicationForms = newForms) }
    }

    private fun saveAllMedications() {
        if (_uiState.value.isLoading) return
        val forms = _uiState.value.medicationForms

        val invalidForm = forms.find { form ->
            validateMedicationEntryUseCase(form.medicationName) is ValidateMedicationEntryUseCase.Result.Error
        }

        if (invalidForm != null) {
            val result = validateMedicationEntryUseCase(invalidForm.medicationName) as ValidateMedicationEntryUseCase.Result.Error
            _uiState.update { it.copy(userMessage = result.error.toUiMessage()) }
            return
        }

        val userId = getCurrentUserIdUseCase()
        if (userId == null) {
            _uiState.update { it.copy(userMessage = MedicationUiMessage.NotLoggedIn) }
            return
        }

        viewModelScope.launch {
            isSaving = true
            _uiState.update { it.copy(isLoading = true) }
            var isAllSuccess = true

            forms.forEach { form ->
                val command = MedicationUiMapper.toSaveCommand(form, userId)

                val result = saveMedicationUseCase(command)
                    .filter { it !is DataResourceResult.Loading }
                    .first()

                if (result is DataResourceResult.Success) {
                } else {
                    isAllSuccess = false
                }
            }

            _uiState.update { currentState ->
                isSaving = false
                if (isAllSuccess) {
                    currentState.copy(
                        isLoading = false,
                        isMedicationCreated = true
                    )
                } else {
                    currentState.copy(
                        isLoading = false,
                        userMessage = MedicationUiMessage.CreateFailed
                    )
                }
            }
        }
    }

    fun toggleMedicationTaken(medicationId: String, scheduleId: String) {
        val userId = getCurrentUserIdUseCase() ?: return

        val currentList = _uiState.value.medicationList
        val targetItem = currentList.find {
            it.medicationId == medicationId && it.scheduleId == scheduleId
        } ?: return

        val newIsTaken = !targetItem.isTaken
        _uiState.update { state ->
            val newList = state.medicationList.map { item ->
                if (item.medicationId == medicationId && item.scheduleId == scheduleId) {
                    item.copy(isTaken = newIsTaken)
                } else {
                    item
                }
            }
            state.copy(medicationList = newList)
        }

        viewModelScope.launch {
            val command = MedicationUiMapper.toToggleCommand(
                uiModel = targetItem,
                userId = userId,
                isTaken = newIsTaken
            )

            toggleIntakeCheckUseCase(command).collectLatest { result ->
                if (result is DataResourceResult.Failure) {
                    result.exception.printStackTrace()
                }
            }
        }
    }

    private fun deleteMedication(medicationId: String) {
        viewModelScope.launch {
            deleteMedicationUseCase(medicationId).collectLatest { result ->
                when (result) {
                    is DataResourceResult.Failure -> {
                        result.exception.printStackTrace()
                    }
                    else -> { }
                }
            }
        }
    }
    // endregion

    // region 4. Helper Functions
    private fun updateForm(index: Int, block: (MedicationFormState) -> MedicationFormState) {
        _uiState.update { state ->
            val updatedForms = state.medicationForms.toMutableList()
            if (index in updatedForms.indices) {
                updatedForms[index] = block(updatedForms[index])
            }
            state.copy(medicationForms = updatedForms)
        }
    }

    private fun calculateProgress(list: List<TodayMedicationUiModel>): MedicationProgressUiModel {
        val total = list.size
        val completed = list.count { it.isTaken }
        if (total == 0) return MedicationProgressUiModel(0f, 0, 0)
        return MedicationProgressUiModel(
            ratio = completed.toFloat() / total,
            completed = completed,
            total = total
        )
    }
    // endregion
}
