package com.moon.pharm.consult.model

import com.moon.pharm.domain.model.ConsultItem
import com.moon.pharm.domain.model.Pharmacist

data class ConsultUiModel (
    val items: List<ConsultItem> = emptyList(),
    val isLoading: Boolean = false,
    val selectedTab: ConsultPrimaryTab = ConsultPrimaryTab.LATEST,
    val selectedItem: ConsultItem? = null,
    val pharmacist: Pharmacist? = null,
    val errorMessage: String? = null
)
