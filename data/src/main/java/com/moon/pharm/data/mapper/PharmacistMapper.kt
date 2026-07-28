package com.moon.pharm.data.mapper

import com.moon.pharm.data.datasource.remote.dto.PharmacistDTO
import com.moon.pharm.domain.model.auth.Pharmacist

fun PharmacistDTO.toDomain(): Pharmacist = Pharmacist(
    userId = userId,
    name = name,
    bio = bio.orEmpty(),
    placeId = placeId,
    pharmacyName = pharmacyName,
    isApproved = isApproved
)

fun Pharmacist.toDto(): PharmacistDTO = PharmacistDTO(
    userId = userId,
    name = name,
    bio = bio,
    placeId = placeId,
    pharmacyName = pharmacyName,
    isApproved = isApproved
)
