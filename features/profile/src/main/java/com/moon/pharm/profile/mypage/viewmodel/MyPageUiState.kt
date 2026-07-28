package com.moon.pharm.profile.mypage.viewmodel

import androidx.compose.ui.graphics.vector.ImageVector
import com.moon.pharm.designsystem.common.UiMessage
import com.moon.pharm.profile.mypage.model.MyPageConsultUiModel
import com.moon.pharm.profile.mypage.model.MyPageUserUiModel

data class MyPageUiState(
    val isLoading: Boolean = true,
    val userMessage: UiMessage? = null,
    val user: MyPageUserUiModel? = null,
    val myConsults: List<MyPageConsultUiModel> = emptyList(),
    val consultHistoryText: String? = null,

    val menuItems: List<MyPageMenuState> = emptyList(),
    val supportItems: List<MyPageMenuState> = emptyList()
)

data class MyPageMenuState(
    val icon: ImageVector,
    val titleResId: Int,
    val actionId: String
)
