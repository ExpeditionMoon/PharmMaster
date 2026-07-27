package com.moon.pharm.profile.auth.model

data class SignUpPharmacyUiModel(
    val id: String = "",
    val placeId: String,
    val name: String,
    val address: String,
    val tel: String,
    val latitude: Double,
    val longitude: Double
)
