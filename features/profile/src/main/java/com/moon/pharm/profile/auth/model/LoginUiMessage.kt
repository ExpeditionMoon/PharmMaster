package com.moon.pharm.profile.auth.model

import com.moon.pharm.designsystem.common.UiMessage

sealed interface LoginUiMessage : UiMessage {
    object EmptyEmail : LoginUiMessage
    object EmptyPassword : LoginUiMessage
    object LoginFailed : LoginUiMessage
}