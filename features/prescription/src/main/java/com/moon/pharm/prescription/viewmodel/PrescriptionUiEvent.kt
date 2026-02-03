package com.moon.pharm.prescription.viewmodel

sealed interface PrescriptionUiEvent {
    data class NavigateToCreate(
        val name: String,
        val dailyCount: Int
    ) : PrescriptionUiEvent
}