package com.moon.pharm.domain.model.prescription

sealed class PrescriptionException : Exception() {
    data object Gemini : PrescriptionException()
    data object Network : PrescriptionException()
    data object OcrNoText : PrescriptionException()
    data object DrugNameNotFound : PrescriptionException()
    data object OcrFailed : PrescriptionException()
}
