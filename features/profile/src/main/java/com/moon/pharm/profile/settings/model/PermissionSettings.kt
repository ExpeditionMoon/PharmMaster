package com.moon.pharm.profile.settings.model

data class PermissionSettings(
    val locationPermission: LocationPermission,
    val appNotification: NotificationPermission,
    val medicationNotification: NotificationPermission,
    val consultNotification: NotificationPermission
) {
    val requiresSystemSettings: Boolean
        get() = locationPermission == LocationPermission.NotGranted ||
            listOf(appNotification, medicationNotification, consultNotification)
                .any { it == NotificationPermission.Disabled }
}

enum class LocationPermission {
    Precise,
    Approximate,
    NotGranted
}

enum class NotificationPermission {
    Enabled,
    Disabled
}
