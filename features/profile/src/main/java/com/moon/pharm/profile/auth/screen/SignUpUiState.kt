package com.moon.pharm.profile.auth.screen

import com.moon.pharm.domain.model.auth.AuthError
import com.moon.pharm.profile.auth.model.SignUpStep

data class SignUpUiState(
    val currentStep: SignUpStep = SignUpStep.TYPE,
    val userType: String = "일반",
    val email: String = "",
    val password: String = "",
    val nickName: String = "",
    val profileImageUri: String? = null,
    val isEmailChecking: Boolean = false,
    val isEmailAvailable: Boolean? = null,
    val isLoading: Boolean = false,
    val error: AuthError? = null,
    val isComplete: Boolean = false
)