package com.moon.pharm.domain.model.auth

data class Pharmacist(
    val userId: String,
    val name: String,
    val bio: String,
    val placeId: String,
    val pharmacyName: String,
    val isApproved: Boolean = false,
)