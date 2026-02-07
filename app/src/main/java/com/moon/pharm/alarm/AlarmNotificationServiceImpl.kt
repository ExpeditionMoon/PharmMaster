package com.moon.pharm.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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

    override fun showMedicationAlarm(name: String, dosage: String, time: String, isGrouped: Boolean) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val groupKey = if (isGrouped) {
            "${AlarmConstants.GROUP_KEY_PREFIX_TIME}$time"
        } else {
            "${AlarmConstants.GROUP_KEY_PREFIX_SINGLE}${name}_$time"
        }
        val summaryId = groupKey.hashCode()
        val notificationId = System.currentTimeMillis().toInt()

        val title = context.getString(R.string.notification_title)
        val content = context.getString(R.string.notification_content, time, name, dosage)

        createNotificationChannel(notificationManager)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(AlarmConstants.KEY_IS_FROM_ALARM, true)
            putExtra(AlarmConstants.KEY_TARGET_FRAGMENT, AlarmConstants.FRAGMENT_MEDICATION)
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
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
            .setGroup(groupKey)

        val summaryNotification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_pill)
            .setContentTitle(time)
            .setContentText(context.getString(R.string.notification_title))
            .setStyle(NotificationCompat.InboxStyle()
                .setSummaryText(context.getString(R.string.notification_summary_text, time)))
            .setGroup(groupKey)
            .setGroupSummary(true)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationId, builder.build())
        notificationManager.notify(summaryId, summaryNotification)
    }

    private fun createNotificationChannel(manager: NotificationManager) {
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