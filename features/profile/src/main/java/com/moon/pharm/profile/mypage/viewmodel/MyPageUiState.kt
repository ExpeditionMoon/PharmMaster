package com.moon.pharm.profile.mypage.viewmodel

import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.model.consult.ConsultItem

data class MyPageUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val myConsults: List<ConsultItem> = emptyList(),
    val error: String? = null
)