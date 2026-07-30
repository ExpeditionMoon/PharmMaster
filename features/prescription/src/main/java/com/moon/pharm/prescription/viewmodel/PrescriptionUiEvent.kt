package com.moon.pharm.prescription.viewmodel

sealed interface PrescriptionUiEvent {
    data class NavigateToCreate(
        val scannedMedicationNames: List<String>
    ) : PrescriptionUiEvent
}
