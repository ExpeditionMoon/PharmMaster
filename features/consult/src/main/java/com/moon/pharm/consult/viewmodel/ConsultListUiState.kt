package com.moon.pharm.consult.viewmodel

import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.consult.model.ConsultPrimaryTab
import com.moon.pharm.domain.model.consult.ConsultItem

data class ConsultListUiState(
    val isLoading: Boolean = false,
    val userMessage: UiMessage? = null,
    val consultList: List<ConsultItem> = emptyList(),
    val selectedTab: ConsultPrimaryTab = ConsultPrimaryTab.LATEST,
    val currentUserId: String? = null,
    val isPharmacist: Boolean = false
)