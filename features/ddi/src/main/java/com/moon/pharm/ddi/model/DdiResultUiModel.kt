package com.moon.pharm.ddi.model

data class DdiResultUiModel(
    val riskLevel: RiskLevelUi,
    val interactionSummary: String,
    val details: List<String>
)

enum class RiskLevelUi {
    HIGH, MODERATE, LOW, UNKNOWN
}