package com.moon.pharm.consult.viewmodel

import com.moon.pharm.consult.model.PharmacistUiModel
import com.moon.pharm.consult.model.PharmacyUiModel
import com.moon.pharm.designsystem.common.UiMessage

data class ConsultWriteUiState(
    val isLoading: Boolean = false,
    val userMessage: UiMessage? = null,
    val isConsultCreated: Boolean = false,

    val title: String = "",
    val content: String = "",
    val images: List<String> = emptyList(),
    val isPublic: Boolean = true,

    val searchQuery: String = "",
    val searchResults: List<PharmacyUiModel> = emptyList(),
    val selectedPharmacy: PharmacyUiModel? = null,
    val availablePharmacists: List<PharmacistUiModel> = emptyList(),
    val selectedPharmacistId: String? = null,

    val isEditMode: Boolean = false
)
