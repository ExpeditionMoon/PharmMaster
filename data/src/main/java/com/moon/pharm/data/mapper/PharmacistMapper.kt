package com.moon.pharm.data.mapper

import com.moon.pharm.data.datasource.remote.dto.PharmacistDTO
import com.moon.pharm.domain.model.auth.Pharmacist

fun PharmacistDTO.toDomain(): Pharmacist {
    return Pharmacist(
        userId = this.userId,
        name = this.name,
        bio = this.bio ?: "",
        placeId = this.placeId,
        pharmacyName = this.pharmacyName,
        isApproved = this.isApproved
    )
}

fun Pharmacist.toDto(): PharmacistDTO {
    return PharmacistDTO(
        userId = this.userId,
        name = this.name,
        bio = this.bio,
        placeId = this.placeId,
        pharmacyName = this.pharmacyName,
        isApproved = this.isApproved
    )
}