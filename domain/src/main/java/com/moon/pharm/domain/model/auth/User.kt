package com.moon.pharm.domain.model.auth

data class User(
    val id: String,
    val email: String,
    val nickName: String,
    val userType: String,
    val profileImageUrl: String? = null,
    val createdAt: Long
)