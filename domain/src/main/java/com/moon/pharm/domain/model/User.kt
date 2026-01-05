package com.moon.pharm.domain.model

data class User(
    val id: String,
    val email: String,
    val nickname: String,
    val userType: String,
    val profileImageUrl: String? = null
)