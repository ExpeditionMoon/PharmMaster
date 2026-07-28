package com.moon.pharm.data.datasource.remote.openapi

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DrugSearchResponse(
    @param:Json(name = "header") val header: DrugSearchHeader?,
    @param:Json(name = "body") val body: DrugSearchBody?
)

@JsonClass(generateAdapter = true)
data class DrugSearchHeader(
    @param:Json(name = "resultCode") val resultCode: String?,
    @param:Json(name = "resultMsg") val resultMsg: String?
)

@JsonClass(generateAdapter = true)
data class DrugSearchBody(
    @param:Json(name = "pageNo") val pageNo: Int?,
    @param:Json(name = "totalCount") val totalCount: Int?,
    @param:Json(name = "numOfRows") val numOfRows: Int?,
    @param:Json(name = "items") val items: List<DrugSearchItem>?
)

@JsonClass(generateAdapter = true)
data class DrugSearchItem(
    @param:Json(name = "itemSeq") val itemSeq: String?,
    @param:Json(name = "itemName") val itemName: String?,
    @param:Json(name = "entpName") val entpName: String?,
    @param:Json(name = "efcyQesitm") val efcyQesitm: String?,
    @param:Json(name = "useMethodQesitm") val useMethodQesitm: String?,
    @param:Json(name = "intrcQesitm") val intrcQesitm: String?,
    @param:Json(name = "itemImage") val itemImage: String?
)
