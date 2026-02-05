package com.moon.pharm.consult.viewmodel

import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.domain.model.consult.ConsultItem

data class MyConsultListUiState(
    val isLoading: Boolean = false,
    val userMessage: UiMessage? = null,
    val myConsults: List<ConsultItem> = emptyList(),
    val isPharmacist: Boolean = false,
    val currentUserId: String? = null
)