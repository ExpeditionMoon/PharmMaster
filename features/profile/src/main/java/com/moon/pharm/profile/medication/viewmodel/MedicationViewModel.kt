package com.moon.pharm.profile.medication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.component_ui.util.toDisplayTimeString
import com.moon.pharm.component_ui.util.toLocalDate
import com.moon.pharm.domain.model.MealTiming
import com.moon.pharm.domain.model.MedicationTimeGroup
import com.moon.pharm.domain.model.MedicationType
import com.moon.pharm.domain.model.RepeatType
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.medication.MedicationUseCases
import com.moon.pharm.profile.medication.model.MedicationPrimaryTab
import com.moon.pharm.profile.medication.screen.MedicationUiState
import com.moon.pharm.profile.medication.common.toMedicationItem
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
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

sealed class MedicationIntent {
    data class UpdateType(val type: MedicationType) : MedicationIntent()
    data class UpdateName(val name: String) : MedicationIntent()
    data class UpdateDosage(val dosage: String) : MedicationIntent()
    data class UpdateStartDate(val millis: Long?) : MedicationIntent()
    data class UpdateEndDate(val millis: Long?) : MedicationIntent()
    data class UpdatePeriod(val start: LocalDate?, val end: LocalDate?, val noEnd: Boolean) : MedicationIntent()
    data class UpdateMealTiming(val timing: MealTiming) : MedicationIntent()
    data class UpdateAlarmTime(val hour: Int, val minute: Int) : MedicationIntent()
    data class UpdateRepeatType(val type: RepeatType) : MedicationIntent()
    data class UpdateMealAlarm(val enabled: Boolean) : MedicationIntent()
    object SaveMedication : MedicationIntent()
    object ResetNavigation : MedicationIntent()

    data class ToggleTaken(val id: String) : MedicationIntent()
}

@HiltViewModel
class MedicationViewModel @Inject constructor(
    private val medicationUseCases: MedicationUseCases
): ViewModel() {
    private val _uiState = MutableStateFlow(MedicationUiState())
    val uiState = _uiState.asStateFlow()

    val groupedMedications: StateFlow<List<MedicationTimeGroup>> = uiState
        .map { state ->
            val filteredList = when (state.selectedTab) {
                MedicationPrimaryTab.ALL -> state.medicationList
                else -> state.medicationList
            }

            filteredList
                .groupBy { it.alarmTime }
                .map { (time, items) ->
                    MedicationTimeGroup(
                        timeLabel = "${time.toDisplayTimeString()} 복용",
                        items = items
                    )
                }
                .sortedBy { it.items.firstOrNull()?.alarmTime }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val progress = uiState.map { state ->
        val total = state.medicationList.size
        val completed = state.medicationList.count { it.isTaken }
        if (total == 0) 0f to "0/0"
        else (completed.toFloat() / total.toFloat()) to "$completed/$total"
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f to "0/0")

    init {
        fetchMedicationList()
    }

    fun onIntent(intent: MedicationIntent) {
        when (intent) {
            is MedicationIntent.UpdateType -> _uiState.update {
                it.copy(form = it.form.copy(selectedType = intent.type))
            }
            is MedicationIntent.UpdateName -> _uiState.update {
                it.copy(form = it.form.copy(medicationName = intent.name))
            }
            is MedicationIntent.UpdateDosage -> _uiState.update {
                it.copy(form = it.form.copy(medicationDosage = intent.dosage))
            }
            is MedicationIntent.UpdateStartDate -> _uiState.update {
                it.copy(form = it.form.copy(startDate = intent.millis.toLocalDate()))
            }
            is MedicationIntent.UpdateEndDate -> _uiState.update {
                it.copy(form = it.form.copy(endDate = intent.millis.toLocalDate()))
            }
            is MedicationIntent.UpdatePeriod -> _uiState.update {
                it.copy(form = it.form.copy(startDate = intent.start, endDate = intent.end, noEndDate = intent.noEnd))
            }
            is MedicationIntent.UpdateMealTiming -> _uiState.update {
                it.copy(form = it.form.copy(selectedMealTiming = intent.timing))
            }
            is MedicationIntent.UpdateAlarmTime -> _uiState.update {
                it.copy(form = it.form.copy(selectedTime = LocalTime.of(intent.hour, intent.minute)))
            }
            is MedicationIntent.UpdateRepeatType -> _uiState.update {
                it.copy(form = it.form.copy(selectedRepeatType = intent.type))
            }
            is MedicationIntent.UpdateMealAlarm -> _uiState.update {
                it.copy(form = it.form.copy(isMealTimeAlarmEnabled = intent.enabled))
            }
            MedicationIntent.SaveMedication -> saveMedication()
            is MedicationIntent.ResetNavigation -> _uiState.update {
                it.copy(isMedicationCreated = false)
            }

            is MedicationIntent.ToggleTaken -> toggleMedicationTaken(intent.id)
        }
    }

    fun onTabSelected(tab: MedicationPrimaryTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    private fun saveMedication() {
        val form = _uiState.value.form
        if (form.medicationName.isBlank()) {
            _uiState.update { it.copy(userMessage = "약 이름을 입력해주세요.") }
            return
        }

        val newItem = form.toMedicationItem()

        viewModelScope.launch {
            medicationUseCases.createMedicationUseCase(newItem).collectLatest { result ->
                _uiState.update { currentState ->
                    when (result) {
                        is DataResourceResult.Loading -> currentState.copy(isLoading = true)
                        is DataResourceResult.Success -> currentState.copy(isLoading = false, isMedicationCreated = true)
                        is DataResourceResult.Failure -> currentState.copy(
                            isLoading = false,
                            userMessage = result.exception.message ?: "복용 알림 등록 실패"
                        )
                        else -> currentState
                    }
                }
            }
        }
    }

    private fun fetchMedicationList() {
        viewModelScope.launch {
            medicationUseCases.getMedicationItemsUseCase().collectLatest { result ->
                _uiState.update { currentState ->
                    when (result) {
                        is DataResourceResult.Loading -> {
                            currentState.copy(isLoading = true, userMessage = null)
                        }

                        is DataResourceResult.Success -> {
                            currentState.copy(
                                isLoading = false, medicationList = result.resultData
                            )
                        }

                        is DataResourceResult.Failure -> {
                            currentState.copy(
                                isLoading = false,
                                userMessage = result.exception.message ?: "데이터를 불러오지 못했습니다."
                            )
                        }

                        else -> currentState
                    }
                }
            }
        }
    }

    private fun toggleMedicationTaken(id: String) {
        _uiState.update { state ->
            val newList = state.medicationList.map { item ->
                if (item.id == id) item.copy(isTaken = !item.isTaken) else item
            }
            state.copy(medicationList = newList)
        }
    }
}