package com.moon.pharm.ddi.mapper

import com.moon.pharm.ddi.model.DdiResultUiModel
import com.moon.pharm.ddi.model.RiskLevelUi
import com.moon.pharm.domain.model.ddi.DdiResult
import com.moon.pharm.domain.model.ddi.RiskLevel

fun DdiResult.toUiModel(): DdiResultUiModel {
    return DdiResultUiModel(
        riskLevel = riskLevel.toUiModel(),
        interactionSummary = interactionSummary,
        details = details
    )
}

fun RiskLevel.toUiModel(): RiskLevelUi {
    return when (this) {
        RiskLevel.HIGH -> RiskLevelUi.HIGH
        RiskLevel.MODERATE -> RiskLevelUi.MODERATE
        RiskLevel.LOW -> RiskLevelUi.LOW
        RiskLevel.UNKNOWN -> RiskLevelUi.UNKNOWN
    }
}