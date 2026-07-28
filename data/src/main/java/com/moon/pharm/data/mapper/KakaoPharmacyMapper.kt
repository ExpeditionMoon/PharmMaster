package com.moon.pharm.data.mapper

import com.moon.pharm.data.common.DEFAULT_LOCATION_COORDINATE
import com.moon.pharm.data.datasource.remote.dto.PharmacyDTO
import com.moon.pharm.data.datasource.remote.kakao.KakaoPharmacyDocument

fun KakaoPharmacyDocument.toDto(): PharmacyDTO = PharmacyDTO(
    placeId = id,
    name = placeName,
    address = roadAddressName.ifEmpty { addressName },
    tel = phone,
    lat = y.toDoubleOrNull() ?: DEFAULT_LOCATION_COORDINATE,
    lng = x.toDoubleOrNull() ?: DEFAULT_LOCATION_COORDINATE
)
