package com.moon.pharm.profile.auth.viewmodel

import com.moon.pharm.profile.auth.model.LoginUiMessage

sealed interface LoginEffect {
    data class ShowMessage(val message: LoginUiMessage) : LoginEffect
    data object NavigateHome : LoginEffect
}
