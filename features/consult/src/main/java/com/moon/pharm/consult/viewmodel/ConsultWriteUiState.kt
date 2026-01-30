package com.moon.pharm.consult.viewmodel

import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.model.pharmacy.Pharmacy

data class ConsultWriteUiState(
    val isLoading: Boolean = false,
    val userMessage: UiMessage? = null,
    val isConsultCreated: Boolean = false,

    val title: String = "",
    val content: String = "",
    val images: List<String> = emptyList(),
    val isPublic: Boolean = true,

    val searchQuery: String = "",
    val searchResults: List<Pharmacy> = emptyList(),
    val selectedPharmacy: Pharmacy? = null,
    val availablePharmacists: List<Pharmacist> = emptyList(),
    val selectedPharmacistId: String? = null
)