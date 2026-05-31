package com.moon.pharm.consult.viewmodel

import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.model.consult.ConsultItem

data class ConsultDetailUiState(
    val isLoading: Boolean = false,
    val selectedItem: ConsultItem? = null,
    val answerPharmacist: Pharmacist? = null,
    val answerPharmacistProfileUrl: String? = null,
    val canAnswer: Boolean = false,
    val currentUserId: String? = null,
    val isEditingAnswer: Boolean = false,
    val answerContent: String = ""
)
