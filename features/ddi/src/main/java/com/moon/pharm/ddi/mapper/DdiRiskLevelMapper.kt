package com.moon.pharm.ddi.mapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.ddi.R
import com.moon.pharm.ddi.model.RiskLevelUi

@Composable
fun RiskLevelUi.toContainerColor(): Color = when (this) {
    RiskLevelUi.HIGH -> PharmTheme.colors.errorContainer
    RiskLevelUi.MODERATE -> PharmTheme.colors.warningContainer
    RiskLevelUi.LOW -> PharmTheme.colors.successContainer
    RiskLevelUi.UNKNOWN -> PharmTheme.colors.infoContainer
}

@Composable
fun RiskLevelUi.toPointColor(): Color = when (this) {
    RiskLevelUi.HIGH -> PharmTheme.colors.error
    RiskLevelUi.MODERATE -> PharmTheme.colors.warning
    RiskLevelUi.LOW -> PharmTheme.colors.success
    RiskLevelUi.UNKNOWN -> PharmTheme.colors.onInfoContainer
}

@Composable
fun RiskLevelUi.asString(): String = when (this) {
    RiskLevelUi.HIGH -> stringResource(R.string.ddi_risk_high)
    RiskLevelUi.MODERATE -> stringResource(R.string.ddi_risk_moderate)
    RiskLevelUi.LOW -> stringResource(R.string.ddi_risk_low)
    RiskLevelUi.UNKNOWN -> stringResource(R.string.ddi_risk_unknown)
}