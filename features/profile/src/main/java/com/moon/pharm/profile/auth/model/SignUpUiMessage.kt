package com.moon.pharm.profile.auth.model

import com.moon.pharm.component_ui.common.UiMessage

sealed interface SignUpUiMessage : UiMessage {
    // 공통
    data object SignUpFailed : SignUpUiMessage

    // Email
    data object EmptyEmail : SignUpUiMessage
    data object InvalidEmailFormat : SignUpUiMessage
    data object EmailDuplicated : SignUpUiMessage
    data object EmailAvailable : SignUpUiMessage
    data object PasswordTooShort : SignUpUiMessage

    // Nickname
    data object EmptyNickname : SignUpUiMessage

    // Pharmacist
    data object EmptyPharmacy : SignUpUiMessage
    data object EmptyBio : SignUpUiMessage
}