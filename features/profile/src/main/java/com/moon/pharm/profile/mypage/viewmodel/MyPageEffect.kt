package com.moon.pharm.profile.mypage.viewmodel

import com.moon.pharm.component_ui.common.UiMessage

sealed interface MyPageEffect {
    data class ShowMessage(val message: UiMessage) : MyPageEffect
    data object NavigateLogin : MyPageEffect
}
