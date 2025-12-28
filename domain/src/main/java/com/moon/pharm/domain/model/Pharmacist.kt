package com.moon.pharm.domain.model

data class Pharmacist(
    val id: String,
    val name: String,
    val bio: String,
    val pharmacyId: String,
    val pharmacyName: String,
    val isApproved: Boolean = false,
    val imageUrl: String
)