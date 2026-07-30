package com.moon.pharm.data.datasource.remote.fcm

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class FcmSendRequest(
    val targetToken: String,
    val title: String,
    val body: String,
    val consultId: String
)

@JsonClass(generateAdapter = true)
data class FcmSendResponse(
    @param:Json(name = "success") val success: Boolean,
    @param:Json(name = "messageId") val messageId: String? = null,
    @param:Json(name = "error") val error: String? = null
)
