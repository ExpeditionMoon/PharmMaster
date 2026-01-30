package com.moon.pharm.data.datasource.remote.fcm

import retrofit2.http.Body
import retrofit2.http.POST

interface FcmApi {
    @POST("send-notification")
    suspend fun sendNotification(
        @Body request: FcmSendRequest
    ): FcmSendResponse
}