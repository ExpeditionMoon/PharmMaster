package com.moon.pharm.prescription.viewmodel

sealed interface PrescriptionUiState {
    data object Idle : PrescriptionUiState
    data object Loading : PrescriptionUiState
    data class Error(val type: PrescriptionError) : PrescriptionUiState
}

enum class PrescriptionError {
    GEMINI,
    NETWORK,
    EMPTY_RESULT,
    OCR
}
