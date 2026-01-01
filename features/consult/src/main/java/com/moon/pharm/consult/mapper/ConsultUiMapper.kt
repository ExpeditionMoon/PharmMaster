package com.moon.pharm.consult.mapper

import androidx.compose.ui.graphics.Color
import com.moon.pharm.component_ui.theme.onPrimaryLight
import com.moon.pharm.component_ui.theme.onSecondaryLight
import com.moon.pharm.component_ui.theme.primaryLight
import com.moon.pharm.component_ui.theme.secondaryLight
import com.moon.pharm.domain.model.consult.ConsultStatus

fun ConsultStatus.toBackgroundColor(): Color = when(this) {
    ConsultStatus.WAITING -> secondaryLight
    ConsultStatus.COMPLETED -> primaryLight
}

fun ConsultStatus.toTextColor(): Color = when(this) {
    ConsultStatus.WAITING -> onSecondaryLight
    ConsultStatus.COMPLETED -> onPrimaryLight
}