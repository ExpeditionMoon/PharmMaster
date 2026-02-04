package com.moon.pharm.profile.medication.viewmodel

import com.moon.pharm.domain.model.medication.IntakeRecord
import com.moon.pharm.profile.medication.model.HistoryRecordUiModel
import java.time.LocalDate
import java.time.YearMonth

data class MedicationHistoryUiState (
    val isLoading: Boolean = false,
    val selectedMonth: YearMonth,
    val selectedDate: LocalDate,
    val monthlyRecords: List<IntakeRecord> = emptyList(),
    val recordsByDate: Map<String, List<HistoryRecordUiModel>> = emptyMap()
)