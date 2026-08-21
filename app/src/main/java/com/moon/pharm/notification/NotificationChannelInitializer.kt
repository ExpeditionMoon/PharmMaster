package com.moon.pharm.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.moon.pharm.alarm.AlarmConstants
import com.moon.pharm.domain.alarm.NotificationChannelIds

object NotificationChannelInitializer {

    fun initialize(context: Context) {
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(
            NotificationChannel(
                NotificationChannelIds.MEDICATION,
                AlarmConstants.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
        )
        notificationManager.createNotificationChannel(
            NotificationChannel(
                NotificationChannelIds.CONSULT,
                CONSULT_NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
        )
    }

    private const val CONSULT_NOTIFICATION_CHANNEL_NAME = "상담 알림"
}
