package com.moon.pharm.consult.screen

import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.consult.model.ConsultPrimaryTab
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.model.Pharmacist

data class ConsultUiState (
    val isLoading: Boolean = false,
    val userMessage: UiMessage? = null,
    val isConsultCreated: Boolean = false,

    val consultList: List<ConsultItem> = emptyList(),
    val selectedTab: ConsultPrimaryTab = ConsultPrimaryTab.LATEST,

    val selectedItem: ConsultItem? = null,
    val pharmacist: Pharmacist? = null,

    val writeState: ConsultWriteState = ConsultWriteState()
)

data class ConsultWriteState(
    val title: String = "",
    val content: String = "",
    val images: List<String> = emptyList(),
    val expertId: String? = null,
    val pharmacist: Pharmacist? = null
)