package com.moon.pharm.consult.viewmodel

import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.consult.model.ConsultUiModel
import com.moon.pharm.consult.model.PharmacistUiModel

data class ConsultDetailUiState(
    val isLoading: Boolean = false,
    val userMessage: UiMessage? = null,

    val selectedItem: ConsultUiModel? = null,
    val answerPharmacist: PharmacistUiModel? = null,
    val answerPharmacistProfileUrl: String? = null,
    val canAnswer: Boolean = false,
    val currentUserId: String? = null,
    val isEditingAnswer: Boolean = false
)
