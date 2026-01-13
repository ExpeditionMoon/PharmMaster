package com.moon.pharm.domain.model.auth

data class User(
    val id: String,
    val email: String,
    val nickName: String,
    val userType: UserType,
    val profileImageUrl: String? = null,
    val createdAt: Long,

    val fcmToken: String? = null
)