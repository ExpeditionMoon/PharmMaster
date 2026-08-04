package com.moon.pharm.prescription.viewmodel

sealed interface PrescriptionUiEvent {
    data class NavigateToMedicationReview(val scannedMedicationNames: List<String>) : PrescriptionUiEvent
}
