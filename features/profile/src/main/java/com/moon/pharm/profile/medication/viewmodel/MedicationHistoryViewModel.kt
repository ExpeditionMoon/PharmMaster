package com.moon.pharm.profile.medication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.auth.GetCurrentUserIdUseCase
import com.moon.pharm.domain.usecase.medication.DeleteMedicationUseCase
import com.moon.pharm.domain.usecase.medication.GetMedicationHistoryItemsUseCase
import com.moon.pharm.domain.usecase.medication.ToggleIntakeCheckUseCase
import com.moon.pharm.profile.medication.mapper.MedicationUiMapper
import com.moon.pharm.profile.medication.model.MedicationUiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class MedicationHistoryViewModel @Inject constructor(
    private val getMedicationHistoryItemsUseCase: GetMedicationHistoryItemsUseCase,
    private val deleteMedicationUseCase: DeleteMedicationUseCase,
    private val toggleIntakeCheckUseCase: ToggleIntakeCheckUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        MedicationHistoryUiState(
            isLoading = true,
            selectedMonth = YearMonth.now(),
            selectedDate = LocalDate.now()
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        fetchMonthlyRecords(YearMonth.now())
    }

    fun fetchMonthlyRecords(yearMonth: YearMonth) {
        val userId = getCurrentUserIdUseCase() ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, selectedMonth = yearMonth) }

            getMedicationHistoryItemsUseCase(userId, yearMonth).collectLatest { result ->
                _uiState.update { state ->
                    when (result) {
                        is DataResourceResult.Loading -> state.copy(isLoading = true)
                        is DataResourceResult.Success -> state.copy(
                            isLoading = false,
                            recordsByDate = MedicationUiMapper.toHistoryUiModelMap(result.resultData)
                        )
                        is DataResourceResult.Failure -> state.copy(isLoading = false)
                    }
                }
            }
        }
    }

    fun toggleRecord(medicationId: String, scheduleId: String, isNowTaken: Boolean, date: LocalDate) {
        val userId = getCurrentUserIdUseCase() ?: return

        viewModelScope.launch {
            val command = MedicationUiMapper.toToggleCommand(
                medicationId = medicationId,
                scheduleId = scheduleId,
                userId = userId,
                isTaken = isNowTaken,
                date = date
            )

            toggleIntakeCheckUseCase(command).collectLatest { }
        }
    }

    fun deleteMedication(medicationId: String) {
        if (getCurrentUserIdUseCase() == null) {
            _uiState.update { it.copy(userMessage = MedicationUiMessage.NotLoggedIn) }
            return
        }

        viewModelScope.launch {
            val result = deleteMedicationUseCase(medicationId)
                .filter { it !is DataResourceResult.Loading }
                .first()

            if (result is DataResourceResult.Failure) {
                _uiState.update { it.copy(userMessage = MedicationUiMessage.DeleteFailed) }
            }
        }
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
    }
}
