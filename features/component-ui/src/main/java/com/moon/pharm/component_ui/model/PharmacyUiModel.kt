package com.moon.pharm.component_ui.model

data class PharmacyUiModel(
    val id: String = "",
    val placeId: String,
    val name: String,
    val address: String,
    val tel: String,
    val latitude: Double,
    val longitude: Double
)
