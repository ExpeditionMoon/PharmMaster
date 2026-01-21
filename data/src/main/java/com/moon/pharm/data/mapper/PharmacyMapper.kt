package com.moon.pharm.data.mapper

import com.moon.pharm.data.datasource.remote.dto.PharmacyDTO
import com.moon.pharm.data.datasource.remote.kakao.KakaoPharmacyDocument
import com.moon.pharm.domain.model.pharmacy.Pharmacy

fun PharmacyDTO.toDomain(): Pharmacy {
    return Pharmacy(
        id = this.id,
        placeId = this.placeId,
        name = this.name,
        address = this.address,
        tel = this.tel,
        latitude = this.lat,
        longitude = this.lng
    )
}

fun Pharmacy.toDto(): PharmacyDTO {
    return PharmacyDTO(
        id = this.id,
        placeId = this.placeId,
        name = this.name,
        address = this.address,
        tel = this.tel,
        lat = this.latitude,
        lng = this.longitude
    )
}

fun KakaoPharmacyDocument.toDto(): PharmacyDTO {
    return PharmacyDTO(
        placeId = this.id,
        name = this.placeName,
        address = this.roadAddressName.ifEmpty { this.addressName },
        tel = this.phone,
        lat = this.y.toDoubleOrNull() ?: 0.0,
        lng = this.x.toDoubleOrNull() ?: 0.0
    )
}