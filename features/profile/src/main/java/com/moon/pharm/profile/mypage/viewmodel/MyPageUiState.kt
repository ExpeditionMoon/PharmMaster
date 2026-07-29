package com.moon.pharm.profile.mypage.viewmodel

import androidx.compose.ui.graphics.vector.ImageVector
import com.moon.pharm.designsystem.common.UiMessage
import com.moon.pharm.profile.mypage.model.MyPageConsultUiModel
import com.moon.pharm.profile.mypage.model.MyPageUserUiModel

data class MyPageUiState(
    val isProfileLoading: Boolean = true,
    val isNicknameUpdating: Boolean = false,
    val isLogoutSuccess: Boolean = false,
    val userMessage: UiMessage? = null,
    val user: MyPageUserUiModel? = null,
    val consultState: MyPageConsultState = MyPageConsultState.Loading,

    val menuItems: List<MyPageMenuState> = emptyList(),
    val supportItems: List<MyPageMenuState> = emptyList()
)

sealed interface MyPageConsultState {
    data object Loading : MyPageConsultState
    data object Error : MyPageConsultState
    data class Content(
        val consults: List<MyPageConsultUiModel>,
        val historyText: String?
    ) : MyPageConsultState
}

data class MyPageMenuState(
    val icon: ImageVector,
    val titleResId: Int,
    val actionId: String
)
