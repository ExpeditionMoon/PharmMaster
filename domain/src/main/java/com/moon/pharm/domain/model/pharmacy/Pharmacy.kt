package com.moon.pharm.domain.model.pharmacy

data class Pharmacy(
    val id: String,
    val placeId: String,
    val name: String,
    val address: String,
    val tel: String,
    val latitude: Double,
    val longitude: Double
)