package com.moon.pharm.data.datasource.remote.ai

import com.moon.pharm.domain.model.ddi.DdiResult
import com.moon.pharm.domain.model.ddi.RiskLevel
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DdiAiResponse(
    val riskLevel: RiskLevel,
    val interactionSummary: String,
    val details: List<String>
) {
    fun toDomain(): DdiResult {
        return DdiResult(
            riskLevel = riskLevel,
            interactionSummary = interactionSummary,
            details = details
        )
    }
}