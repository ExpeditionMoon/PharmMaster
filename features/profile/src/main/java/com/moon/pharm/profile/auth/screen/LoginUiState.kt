package com.moon.pharm.profile.auth.screen

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false
)
