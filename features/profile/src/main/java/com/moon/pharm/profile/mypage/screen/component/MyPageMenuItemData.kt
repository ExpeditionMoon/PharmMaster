package com.moon.pharm.profile.mypage.screen.component

import androidx.compose.ui.graphics.vector.ImageVector

data class MyPageMenuItemData(
    val icon: ImageVector,
    val title: String,
    val onClick: () -> Unit
)