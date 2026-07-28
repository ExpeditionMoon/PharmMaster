package com.moon.pharm.profile.auth.screen

import com.moon.pharm.designsystem.common.UiMessage

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isLoginSuccess: Boolean = false,
    val userMessage: UiMessage? = null
)