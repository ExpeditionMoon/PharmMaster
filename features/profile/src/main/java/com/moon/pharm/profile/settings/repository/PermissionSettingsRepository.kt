package com.moon.pharm.profile.settings.repository

import com.moon.pharm.profile.settings.model.PermissionSettings

interface PermissionSettingsRepository {
    fun getPermissionSettings(): PermissionSettings
}
