package com.moon.pharm.data.datasource.remote.dto

import com.moon.pharm.data.common.DEFAULT_LOCATION_COORDINATE
import com.moon.pharm.data.common.EMPTY_STRING

data class PharmacyDTO(
    val id: String = EMPTY_STRING,
    val placeId: String = EMPTY_STRING,
    val name: String = EMPTY_STRING,
    val address: String = EMPTY_STRING,
    val tel: String = EMPTY_STRING,
    val lat: Double = DEFAULT_LOCATION_COORDINATE,
    val lng: Double = DEFAULT_LOCATION_COORDINATE
)