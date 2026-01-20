package com.moon.pharm.profile.auth.screen

import com.moon.pharm.domain.model.auth.AuthError
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.domain.model.pharmacy.Pharmacy
import com.moon.pharm.profile.auth.model.SignUpStep

data class SignUpUiState(
    val currentStep: SignUpStep = SignUpStep.TYPE,
    val userType: UserType? = null,

    val email: String = "",
    val isEmailChecking: Boolean = false,
    val isEmailAvailable: Boolean? = null,
    val password: String = "",

    val nickName: String = "",
    val profileImageUri: String? = null,

    val pharmacyName: String = "",
    val pharmacistBio: String = "",
    val selectedPharmacy: Pharmacy? = null,
    val pharmacySearchResults: List<Pharmacy> = emptyList(),

    val isLoading: Boolean = false,
    val isComplete: Boolean = false,
    val error: AuthError? = null
)