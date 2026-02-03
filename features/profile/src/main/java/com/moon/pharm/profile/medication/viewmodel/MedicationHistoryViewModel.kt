package com.moon.pharm.profile.medication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.component_ui.util.toQueryString
import com.moon.pharm.domain.model.medication.IntakeRecord
import com.moon.pharm.domain.model.medication.Medication
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.auth.GetCurrentUserIdUseCase
import com.moon.pharm.domain.usecase.medication.GetMedicationsUseCase
import com.moon.pharm.domain.usecase.medication.GetMonthlyIntakeRecordsUseCase
import com.moon.pharm.domain.usecase.medication.ToggleIntakeCheckUseCase
import com.moon.pharm.profile.medication.model.HistoryRecordUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class MedicationHistoryViewModel @Inject constructor(
    private val getMonthlyIntakeRecordsUseCase: GetMonthlyIntakeRecordsUseCase,
    private val getMedicationsUseCase: GetMedicationsUseCase,
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
        val currentMonth = YearMonth.now()
        fetchMonthlyRecords(currentMonth)
    }

    fun fetchMonthlyRecords(yearMonth: YearMonth) {
        val userId = getCurrentUserIdUseCase() ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, selectedMonth = yearMonth) }

            val recordsFlow = getMonthlyIntakeRecordsUseCase(userId, yearMonth.toString())
            val medicationsFlow = getMedicationsUseCase(userId)

            combine(recordsFlow, medicationsFlow) { recordsResult, medsResult ->
                when {
                    recordsResult is DataResourceResult.Success && medsResult is DataResourceResult.Success -> {
                        generateFullHistory(
                            yearMonth = yearMonth,
                            medications = medsResult.resultData,
                            realRecords = recordsResult.resultData
                        )
                    }
                    else -> { emptyMap() }
                }
            }.collectLatest { fullRecordsByDate ->
                _uiState.update {
                    it.copy(isLoading = false, recordsByDate = fullRecordsByDate)
                }
            }
        }
    }

    private fun generateFullHistory(
        yearMonth: YearMonth,
        medications: List<Medication>,
        realRecords: List<IntakeRecord>
    ): Map<String, List<HistoryRecordUiModel>> {
        val resultMap = mutableMapOf<String, MutableList<HistoryRecordUiModel>>()

        val firstDay = yearMonth.atDay(1)
        val lastDay = yearMonth.atEndOfMonth()
        var currentDate = firstDay

        while (!currentDate.isAfter(lastDay)) {
            val dateKey = currentDate.toString()
            val dailyList = mutableListOf<HistoryRecordUiModel>()

            medications.forEach { medication ->
                if (isActiveDate(medication, currentDate)) {
                    medication.schedules.forEach { schedule ->

                        val existingRecord = realRecords.find {
                            it.medicationId == medication.id &&
                                    it.recordDate == dateKey &&
                                    it.scheduleId == schedule.id &&
                                    it.isTaken
                        }

                        val finalRecord = existingRecord ?: IntakeRecord(
                            id = "virtual_${medication.id}_${schedule.id}_${dateKey}",
                            userId = medication.userId,
                            medicationId = medication.id,
                            scheduleId = schedule.id,
                            recordDate = dateKey,
                            isTaken = false,
                            takenTime = null
                        )

                        dailyList.add(
                            HistoryRecordUiModel(
                                record = finalRecord,
                                medicationName = medication.name,
                                time = schedule.time
                            )
                        )
                    }
                }
            }
            resultMap[dateKey] = dailyList.sortedBy { it.time }.toMutableList()
            currentDate = currentDate.plusDays(1)
        }
        return resultMap
    }

    fun toggleRecord(medicationId: String, scheduleId: String, isNowTaken: Boolean, date: LocalDate) {
        val userId = getCurrentUserIdUseCase() ?: return
        val dateString = date.toQueryString()

        viewModelScope.launch {
            val record = IntakeRecord(
                id = "",
                userId = userId,
                medicationId = medicationId,
                scheduleId = scheduleId,
                recordDate = dateString,
                isTaken = isNowTaken,
                takenTime = System.currentTimeMillis()
            )

            toggleIntakeCheckUseCase(
                record = record,
                isTaken = isNowTaken
            ).collectLatest { }
        }
    }

    private fun isActiveDate(medication: Medication, date: LocalDate): Boolean {
        val startDate = java.time.Instant.ofEpochMilli(medication.startDate ?: 0)
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDate()
        if (date.isBefore(startDate)) return false

        if (medication.endDate != null) {
            val endDate = java.time.Instant.ofEpochMilli(medication.endDate!!)
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate()
            if (date.isAfter(endDate)) return false
        }
        return true
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
    }
}