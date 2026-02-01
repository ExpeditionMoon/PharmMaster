package com.moon.pharm.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.moon.pharm.MainActivity
import com.moon.pharm.R
import com.moon.pharm.data.common.NOTIFICATION_CHANNEL_ID
import com.moon.pharm.data.common.NOTIFICATION_CHANNEL_NAME
import com.moon.pharm.domain.alarm.AlarmNotificationService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AlarmNotificationServiceImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : AlarmNotificationService {

    override fun showMedicationAlarm(name: String, dosage: String, time: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val title = context.getString(R.string.notification_title)
        val content = context.getString(R.string.notification_content, time, name, dosage)

        createNotificationChannel(notificationManager)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(AlarmConstants.KEY_IS_FROM_ALARM, true)
            putExtra(AlarmConstants.KEY_TARGET_FRAGMENT, AlarmConstants.FRAGMENT_MEDICATION)
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_pill)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }

    private fun createNotificationChannel(manager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (manager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null) {
                val channel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = context.getString(R.string.notification_channel_description)
                    enableVibration(true)
                }
                manager.createNotificationChannel(channel)
            }
        }
    }
}