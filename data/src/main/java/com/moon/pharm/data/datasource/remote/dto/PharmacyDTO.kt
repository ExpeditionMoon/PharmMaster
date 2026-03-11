package com.moon.pharm.data.datasource.remote.dto

import com.moon.pharm.data.common.DEFAULT_LOCATION_COORDINATE
import com.moon.pharm.data.common.EMPTY_STRING
import com.moon.pharm.domain.model.pharmacy.Pharmacy

data class PharmacyDTO(
    val id: String = EMPTY_STRING,
    val placeId: String = EMPTY_STRING,
    val name: String = EMPTY_STRING,
    val address: String = EMPTY_STRING,
    val tel: String = EMPTY_STRING,
    val lat: Double = DEFAULT_LOCATION_COORDINATE,
    val lng: Double = DEFAULT_LOCATION_COORDINATE
)

// Pharmacy 매퍼
fun PharmacyDTO.toDomain(): Pharmacy = Pharmacy(
    id = this.id,
    placeId = this.placeId,
    name = this.name,
    address = this.address,
    tel = this.tel,
    latitude = this.lat,
    longitude = this.lng
)

fun Pharmacy.toDto(): PharmacyDTO = PharmacyDTO(
    id = this.id,
    placeId = this.placeId,
    name = this.name,
    address = this.address,
    tel = this.tel,
    lat = this.latitude,
    lng = this.longitude
)