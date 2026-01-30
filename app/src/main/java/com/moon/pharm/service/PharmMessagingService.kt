package com.moon.pharm.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.moon.pharm.MainActivity
import com.moon.pharm.R
import com.moon.pharm.data.common.NotificationConstants.CHANNEL_ID_CONSULT
import com.moon.pharm.data.common.NotificationConstants.CHANNEL_NAME_CONSULT
import com.moon.pharm.data.common.NotificationConstants.KEY_BODY
import com.moon.pharm.data.common.NotificationConstants.KEY_CONSULT_ID
import com.moon.pharm.data.common.NotificationConstants.KEY_TITLE
import com.moon.pharm.domain.usecase.user.SyncFcmTokenUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PharmMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var syncFcmTokenUseCase: SyncFcmTokenUseCase

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.notification?.title
            ?: remoteMessage.data[KEY_TITLE]
            ?: getString(R.string.noti_default_title)

        val body = remoteMessage.notification?.body
            ?: remoteMessage.data[KEY_BODY]
            ?: getString(R.string.noti_default_body)
        val consultId = remoteMessage.data[KEY_CONSULT_ID]

        showNotification(title, body, consultId)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                syncFcmTokenUseCase()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showNotification(title: String, messageBody: String, consultId: String?) {
        val channelId = CHANNEL_ID_CONSULT
        val channelName = CHANNEL_NAME_CONSULT

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            if (consultId != null) {
                putExtra(KEY_CONSULT_ID, consultId)
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.splash_logo)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = getSystemService(NotificationManager::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}