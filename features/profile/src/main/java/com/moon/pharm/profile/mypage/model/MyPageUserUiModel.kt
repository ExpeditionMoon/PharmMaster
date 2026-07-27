package com.moon.pharm.profile.mypage.model

data class MyPageUserUiModel(
    val id: String,
    val nickName: String,
    val profileImageUrl: String?,
    val isPharmacist: Boolean
)
