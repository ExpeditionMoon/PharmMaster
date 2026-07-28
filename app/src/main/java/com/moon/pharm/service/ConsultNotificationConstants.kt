package com.moon.pharm.service

internal object ConsultNotificationConstants {
    // 채널 정보
    const val CHANNEL_ID = "pharm_consult_channel"
    const val CHANNEL_NAME = "상담 알림"

    // 인텐트 및 데이터 키 (FCM Payload Key)
    const val EXTRA_CONSULT_ID = "consultId"
    const val PAYLOAD_TITLE = "title"
    const val PAYLOAD_BODY = "body"
}
