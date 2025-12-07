package com.moon.pharm.component_ui.model

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomBarUiModel (
    val tabName: String = "",
    val icon: ImageVector,
    val onClick: () -> Unit
)