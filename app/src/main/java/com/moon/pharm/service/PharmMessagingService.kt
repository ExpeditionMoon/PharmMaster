package com.moon.pharm.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.moon.pharm.MainActivity
import com.moon.pharm.R
import com.moon.pharm.domain.usecase.user.SyncFcmTokenUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class PharmMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var syncFcmTokenUseCase: SyncFcmTokenUseCase

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.notification?.title
            ?: remoteMessage.data[ConsultNotificationConstants.PAYLOAD_TITLE]
            ?: getString(R.string.noti_default_title)

        val body = remoteMessage.notification?.body
            ?: remoteMessage.data[ConsultNotificationConstants.PAYLOAD_BODY]
            ?: getString(R.string.noti_default_body)
        val consultId = remoteMessage.data[ConsultNotificationConstants.EXTRA_CONSULT_ID]

        showNotification(title, body, consultId)
    }

    override fun onRegistered(installationId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                syncFcmTokenUseCase(installationId)
            } catch (_: Exception) {
                return@launch
            }
        }
    }

    private fun showNotification(title: String, messageBody: String, consultId: String?) {
        val channelId = ConsultNotificationConstants.CHANNEL_ID
        val channelName = ConsultNotificationConstants.CHANNEL_NAME

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            if (consultId != null) {
                putExtra(ConsultNotificationConstants.EXTRA_CONSULT_ID, consultId)
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

        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}
