package com.moon.pharm.component_ui.model

import androidx.compose.ui.graphics.vector.ImageVector

data class TopBarData(
    val title: String = "",
    val isLogoTitle: Boolean = false,
    val navigationType: TopBarNavigationType = TopBarNavigationType.None,
    val onNavigationClick: () -> Unit = {},
    val actions: List<TopBarAction> = emptyList()
)

enum class TopBarNavigationType {
    None, Back, Menu, Close
}

data class TopBarAction(
    val icon: ImageVector? = null,
    val text: String? = null,
    val onClick: () -> Unit = {}
)