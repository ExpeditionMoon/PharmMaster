package com.moon.pharm.consult.mapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.domain.model.consult.ConsultStatus

@Composable
fun ConsultStatus.toBackgroundColor(): Color = when(this) {
    ConsultStatus.WAITING -> PharmTheme.colors.secondary
    ConsultStatus.COMPLETED -> PharmTheme.colors.primary
}

@Composable
fun ConsultStatus.toTextColor(): Color = when(this) {
    ConsultStatus.WAITING -> PharmTheme.colors.onSecondary
    ConsultStatus.COMPLETED -> PharmTheme.colors.onPrimary
}