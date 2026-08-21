package com.moon.pharm.profile.settings.repository

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.moon.pharm.domain.alarm.NotificationChannelIds
import com.moon.pharm.profile.settings.model.LocationPermission
import com.moon.pharm.profile.settings.model.NotificationPermission
import com.moon.pharm.profile.settings.model.PermissionSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidPermissionSettingsRepository @Inject constructor(
    @param:ApplicationContext private val context: Context
) : PermissionSettingsRepository {

    override fun getPermissionSettings(): PermissionSettings {
        val isAppNotificationEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled()

        return PermissionSettings(
            locationPermission = getLocationPermission(),
            appNotification = isAppNotificationEnabled.toNotificationPermission(),
            medicationNotification = getChannelNotificationPermission(
                channelId = NotificationChannelIds.MEDICATION,
                isAppNotificationEnabled = isAppNotificationEnabled
            ),
            consultNotification = getChannelNotificationPermission(
                channelId = NotificationChannelIds.CONSULT,
                isAppNotificationEnabled = isAppNotificationEnabled
            )
        )
    }

    private fun getLocationPermission(): LocationPermission = when {
        context.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) -> LocationPermission.Precise
        context.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION) -> LocationPermission.Approximate
        else -> LocationPermission.NotGranted
    }

    private fun getChannelNotificationPermission(
        channelId: String,
        isAppNotificationEnabled: Boolean
    ): NotificationPermission {
        val channel = context.getSystemService(NotificationManager::class.java)
            .getNotificationChannel(channelId)
        val isChannelEnabled = channel?.importance != NotificationManager.IMPORTANCE_NONE

        return (isAppNotificationEnabled && isChannelEnabled).toNotificationPermission()
    }

    private fun Context.hasPermission(permission: String): Boolean =
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    private fun Boolean.toNotificationPermission(): NotificationPermission =
        if (this) NotificationPermission.Enabled else NotificationPermission.Disabled
}
