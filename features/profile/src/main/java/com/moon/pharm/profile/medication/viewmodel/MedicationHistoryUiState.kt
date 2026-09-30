package com.moon.pharm.profile.medication.viewmodel

import com.moon.pharm.designsystem.common.UiMessage
import com.moon.pharm.profile.medication.model.HistoryRecordUiModel
import java.time.LocalDate
import java.time.YearMonth

data class MedicationHistoryUiState (
    val isLoading: Boolean = false,
    val userMessage: UiMessage? = null,
    val selectedMonth: YearMonth,
    val selectedDate: LocalDate,
    val recordsByDate: Map<String, List<HistoryRecordUiModel>> = emptyMap()
)
