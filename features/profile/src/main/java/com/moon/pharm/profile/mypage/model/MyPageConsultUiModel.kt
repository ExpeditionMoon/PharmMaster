package com.moon.pharm.profile.mypage.model

data class MyPageConsultUiModel(
    val id: String,
    val status: MyPageConsultStatusUiModel
)

enum class MyPageConsultStatusUiModel {
    Waiting,
    Completed
}
