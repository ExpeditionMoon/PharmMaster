package com.moon.pharm.designsystem.model

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomBarUiModel (
    val tabName: String = "",
    val icon: ImageVector,
    val onClick: () -> Unit
)