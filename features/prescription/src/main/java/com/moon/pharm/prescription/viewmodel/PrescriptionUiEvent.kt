package com.moon.pharm.prescription.viewmodel

import com.moon.pharm.component_ui.model.ScannedMedication

sealed interface PrescriptionUiEvent {
    data class NavigateToCreate(
        val scannedList: List<ScannedMedication>
    ) : PrescriptionUiEvent
}