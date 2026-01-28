package com.moon.pharm.consult.viewmodel

import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.model.consult.ConsultItem

data class ConsultDetailUiState(
    val isLoading: Boolean = false,
    val userMessage: UiMessage? = null,

    val selectedItem: ConsultItem? = null,
    val answerPharmacist: Pharmacist? = null,
    val answerPharmacistProfileUrl: String? = null,
    val canAnswer: Boolean = false
)