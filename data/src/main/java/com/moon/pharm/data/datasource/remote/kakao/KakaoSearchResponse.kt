package com.moon.pharm.data.datasource.remote.kakao

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KakaoSearchResponse(
    @Json(name = "documents") val documents: List<KakaoPharmacyDocument>,
    @Json(name = "meta") val meta: KakaoMeta
)

@JsonClass(generateAdapter = true)
data class KakaoPharmacyDocument(
    @Json(name = "id") val id: String,
    @Json(name = "place_name") val placeName: String,
    @Json(name = "phone") val phone: String,
    @Json(name = "address_name") val addressName: String,
    @Json(name = "road_address_name") val roadAddressName: String,
    @Json(name = "x") val x: String,
    @Json(name = "y") val y: String,
    @Json(name = "place_url") val placeUrl: String,
    @Json(name = "distance") val distance: String
)

@JsonClass(generateAdapter = true)
data class KakaoMeta(
    @Json(name = "total_count") val totalCount: Int,
    @Json(name = "is_end") val isEnd: Boolean
)