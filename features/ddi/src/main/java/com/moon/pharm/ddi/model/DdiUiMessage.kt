package com.moon.pharm.ddi.model

sealed interface DdiUiMessage {
    data object AnalysisFailed : DdiUiMessage
    data object NetworkError : DdiUiMessage
    data object EmptyDrug : DdiUiMessage
    data object Unknown : DdiUiMessage
    data class DynamicError(val message: String) : DdiUiMessage
}