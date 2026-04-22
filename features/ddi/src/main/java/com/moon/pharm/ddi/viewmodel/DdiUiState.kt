package com.moon.pharm.ddi.viewmodel

import com.moon.pharm.ddi.model.DdiResultUiModel
import com.moon.pharm.ddi.model.DdiUiMessage

data class DdiUiState(
    val selectedDrugs: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val userMessage: DdiUiMessage? = null,
    val result: DdiResultUiModel? = null
)