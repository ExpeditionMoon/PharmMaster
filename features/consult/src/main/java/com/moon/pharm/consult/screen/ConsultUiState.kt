package com.moon.pharm.consult.screen

import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.consult.model.ConsultPrimaryTab
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.model.pharmacy.Pharmacy

data class ConsultUiState (
    val isLoading: Boolean = false,
    val userMessage: UiMessage? = null,
    val isConsultCreated: Boolean = false,

    val consultList: List<ConsultItem> = emptyList(),
    val selectedTab: ConsultPrimaryTab = ConsultPrimaryTab.LATEST,

    val selectedItem: ConsultItem? = null,
    val answerPharmacist: Pharmacist? = null,
    val answerPharmacistProfileUrl: String? = null,

    val writeState: ConsultWriteState = ConsultWriteState()
)

data class ConsultWriteState(
    val title: String = "",
    val content: String = "",
    val images: List<String> = emptyList(),

    val searchQuery: String = "",
    val searchResults: List<Pharmacy> = emptyList(),
    val selectedPharmacy: Pharmacy? = null,
    val availablePharmacists: List<Pharmacist> = emptyList(),
    val selectedPharmacistId: String? = null
)