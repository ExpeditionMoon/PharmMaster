package com.moon.pharm.domain.model.map

data class GeoLocation(
    val lat: Double,
    val lng: Double
) {
    companion object {
        val DEFAULT = GeoLocation(37.5665, 126.9780)
    }
}