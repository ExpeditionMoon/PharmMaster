package com.moon.pharm.consult.mapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.consult.model.ConsultStatusUiModel

@Composable
fun ConsultStatusUiModel.toBackgroundColor(): Color = when(this) {
    ConsultStatusUiModel.Waiting -> PharmTheme.colors.secondary
    ConsultStatusUiModel.Completed -> PharmTheme.colors.primary
}

@Composable
fun ConsultStatusUiModel.toTextColor(): Color = when(this) {
    ConsultStatusUiModel.Waiting -> PharmTheme.colors.onSecondary
    ConsultStatusUiModel.Completed -> PharmTheme.colors.onPrimary
}
