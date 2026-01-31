package com.moon.pharm.profile.mypage.viewmodel

import androidx.compose.ui.graphics.vector.ImageVector
import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.model.consult.ConsultItem

data class MyPageUiState(
    val isLoading: Boolean = true,
    val userMessage: UiMessage? = null,
    val user: User? = null,
    val myConsults: List<ConsultItem> = emptyList(),
    val consultHistoryText: String? = null,

    val menuItems: List<MyPageMenuState> = emptyList(),
    val supportItems: List<MyPageMenuState> = emptyList()
)

data class MyPageMenuState(
    val icon: ImageVector,
    val titleResId: Int,
    val actionId: String
)