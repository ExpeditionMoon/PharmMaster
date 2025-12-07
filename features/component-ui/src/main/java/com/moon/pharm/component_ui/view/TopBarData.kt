package com.moon.pharm.component_ui.view

import androidx.compose.material.icons.Icons.AutoMirrored
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.graphics.vector.ImageVector

data class TopBarData(
    val title: String = "",
//    var titleIcon: ImageVector = AutoMirrored.Filled.ArrowBack,
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