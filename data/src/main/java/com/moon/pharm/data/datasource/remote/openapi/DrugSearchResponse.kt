package com.moon.pharm.data.datasource.remote.openapi

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DrugSearchResponse(
    @Json(name = "header") val header: DrugSearchHeader?,
    @Json(name = "body") val body: DrugSearchBody?
)

@JsonClass(generateAdapter = true)
data class DrugSearchHeader(
    @Json(name = "resultCode") val resultCode: String?,
    @Json(name = "resultMsg") val resultMsg: String?
)

@JsonClass(generateAdapter = true)
data class DrugSearchBody(
    @Json(name = "pageNo") val pageNo: Int?,
    @Json(name = "totalCount") val totalCount: Int?,
    @Json(name = "numOfRows") val numOfRows: Int?,
    @Json(name = "items") val items: List<DrugSearchItem>?
)

@JsonClass(generateAdapter = true)
data class DrugSearchItem(
    @Json(name = "itemSeq") val itemSeq: String?,
    @Json(name = "itemName") val itemName: String?,
    @Json(name = "entpName") val entpName: String?,
    @Json(name = "efcyQesitm") val efcyQesitm: String?,
    @Json(name = "useMethodQesitm") val useMethodQesitm: String?,
    @Json(name = "intrcQesitm") val intrcQesitm: String?,
    @Json(name = "itemImage") val itemImage: String?
)