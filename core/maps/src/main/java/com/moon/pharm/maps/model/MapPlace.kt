package com.moon.pharm.maps.model

data class MapPlace(
    val id: String = "",
    val placeId: String,
    val name: String,
    val address: String,
    val tel: String,
    val latitude: Double,
    val longitude: Double
)
