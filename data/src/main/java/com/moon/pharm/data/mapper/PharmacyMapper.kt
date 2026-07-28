package com.moon.pharm.data.mapper

import com.moon.pharm.data.datasource.remote.dto.PharmacyDTO
import com.moon.pharm.domain.model.pharmacy.Pharmacy

fun PharmacyDTO.toDomain(): Pharmacy = Pharmacy(
    id = id,
    placeId = placeId,
    name = name,
    address = address,
    tel = tel,
    latitude = lat,
    longitude = lng
)

fun Pharmacy.toDto(): PharmacyDTO = PharmacyDTO(
    id = id,
    placeId = placeId,
    name = name,
    address = address,
    tel = tel,
    lat = latitude,
    lng = longitude
)
