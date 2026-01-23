package com.moon.pharm.profile.auth.screen

import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.domain.model.pharmacy.Pharmacy
import com.moon.pharm.profile.auth.model.SignUpStep

data class SignUpUiState(
    // 1. 진행 단계
    val currentStep: SignUpStep = SignUpStep.TYPE,

    // 2. 일반 사용자 입력 데이터
    val userType: UserType? = null,
    val email: String = "",
    val password: String = "",
    val nickName: String = "",
    val profileImageUri: String? = null,

    // 3. 약사 전용 입력 데이터
    val pharmacyName: String = "",
    val selectedPharmacy: Pharmacy? = null,
    val pharmacistBio: String = "",

    // 4. 화면 상태 플래그
    val isLoading: Boolean = false,
    val isEmailChecking: Boolean = false,
    val isEmailAvailable: Boolean? = null,
    val isComplete: Boolean = false,

    // 5. 검색 데이터
    val pharmacySearchResults: List<Pharmacy> = emptyList(),

    // 6. 에러 메시지
    val userMessage: UiMessage? = null
)