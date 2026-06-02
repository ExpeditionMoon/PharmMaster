package com.moon.pharm.component_ui.model

data class PharmacistUiModel(
    val userId: String,
    val name: String,
    val bio: String,
    val placeId: String,
    val pharmacyName: String,
    val isApproved: Boolean = false
)
