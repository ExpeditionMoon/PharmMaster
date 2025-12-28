package com.moon.pharm.profile.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.domain.model.MealTiming
import com.moon.pharm.domain.model.MedicationItem
import com.moon.pharm.domain.model.MedicationType
import com.moon.pharm.domain.model.RepeatType
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.medication.MedicationUseCases
import com.moon.pharm.profile.screen.medication.MedicationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class MedicationViewModel @Inject constructor(
    private val medicationUseCases: MedicationUseCases
): ViewModel() {
    private val _uiState = MutableStateFlow(MedicationUiState())
    private val _medication = MutableStateFlow<List<MedicationItem>>(emptyList())
    val uiState = _uiState.asStateFlow()
    val medication: StateFlow<List<MedicationItem>> = _medication.asStateFlow()

    fun updateName(name: String) {
        _uiState.update { it.copy(form = it.form.copy(medicationName = name)) }
    }

    fun updateDosage(dosage: String) {
        _uiState.update { it.copy(form = it.form.copy(medicationDosage = dosage)) }
    }

    fun updateType(type: MedicationType) {
        _uiState.update { it.copy(form = it.form.copy(selectedType = type)) }
    }

    fun updatePeriod(start: LocalDate?, end: LocalDate?, noEnd: Boolean) {
        _uiState.update { it.copy(form = it.form.copy(startDate = start, endDate = end, noEndDate = noEnd)) }
    }

    fun updateMealTiming(timing: MealTiming) {
        _uiState.update { it.copy(form = it.form.copy(selectedMealTiming = timing)) }
    }

    @SuppressLint("NewApi")
    fun updateAlarmTime(hour: Int, minute: Int) {
        _uiState.update { it.copy(form = it.form.copy(selectedTime = LocalTime.of(hour, minute))) }
    }

    fun updateRepeatType(type: RepeatType) {
        _uiState.update { it.copy(form = it.form.copy(selectedRepeatType = type)) }
    }

    fun updateMealTimeAlarmEnabled(enabled: Boolean) {
        _uiState.update { it.copy(form = it.form.copy(isMealTimeAlarmEnabled = enabled)) }
    }

    fun createMedication(medicationItem: MedicationItem) {
        viewModelScope.launch {
            medicationUseCases.createMedicationUseCase(medicationItem).collect { result ->
                _uiState.update { currentState ->
                    when (result) {
                        is DataResourceResult.Loading -> {
                            currentState.copy(isLoading = true, userMessage = null)
                        }

                        is DataResourceResult.Success -> {
                            currentState.copy(
                                isLoading = false, isMedicationCreated = true
                            )
                        }

                        is DataResourceResult.Failure -> {
                            currentState.copy(
                                isLoading = false,
                                userMessage = result.exception.message ?: "복용 알림 등록 실패"
                            )
                        }

                        else -> currentState
                    }
                }
            }
        }
    }

    fun saveMedication() {
        val form = _uiState.value.form
        if (form.medicationName.isBlank()) {
            _uiState.update { it.copy(userMessage = "약 이름을 입력해주세요.") }
            return
        }
        val newItem = MedicationItem(
            id = "",
            name = form.medicationName,
            dosage = form.medicationDosage,
            type = form.selectedType,
            startDate = form.startDate,
            endDate = form.endDate,
            noEndDate = form.noEndDate,
            alarmTime = form.selectedTime,
            mealTiming = form.selectedMealTiming,
            repeatType = form.selectedRepeatType
        )
        createMedication(newItem)
    }
}