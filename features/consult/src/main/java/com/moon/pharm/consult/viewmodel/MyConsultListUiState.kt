package com.moon.pharm.consult.viewmodel

import com.moon.pharm.consult.model.ConsultUiModel
import com.moon.pharm.designsystem.common.UiMessage

data class MyConsultListUiState(
    val isLoading: Boolean = false,
    val userMessage: UiMessage? = null,
    val myConsults: List<ConsultUiModel> = emptyList(),
    val isPharmacist: Boolean = false,
    val currentUserId: String? = null
)
