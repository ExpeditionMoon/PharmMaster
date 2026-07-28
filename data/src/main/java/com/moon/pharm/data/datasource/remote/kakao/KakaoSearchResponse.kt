package com.moon.pharm.data.datasource.remote.kakao

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KakaoSearchResponse(
    @param:Json(name = "documents") val documents: List<KakaoPharmacyDocument>,
    @param:Json(name = "meta") val meta: KakaoMeta
)

@JsonClass(generateAdapter = true)
data class KakaoPharmacyDocument(
    @param:Json(name = "id") val id: String,
    @param:Json(name = "place_name") val placeName: String,
    @param:Json(name = "phone") val phone: String,
    @param:Json(name = "address_name") val addressName: String,
    @param:Json(name = "road_address_name") val roadAddressName: String,
    @param:Json(name = "x") val x: String,
    @param:Json(name = "y") val y: String,
    @param:Json(name = "place_url") val placeUrl: String,
    @param:Json(name = "distance") val distance: String
)

@JsonClass(generateAdapter = true)
data class KakaoMeta(
    @param:Json(name = "total_count") val totalCount: Int,
    @param:Json(name = "is_end") val isEnd: Boolean
)
