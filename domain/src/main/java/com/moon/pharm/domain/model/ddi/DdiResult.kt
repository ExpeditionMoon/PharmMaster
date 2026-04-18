package com.moon.pharm.domain.model.ddi

data class DdiResult(
    val riskLevel: RiskLevel,
    val interactionSummary: String,
    val details: List<String>
)

enum class RiskLevel {
    HIGH,
    MODERATE,
    LOW,
    UNKNOWN
}