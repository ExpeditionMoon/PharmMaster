package com.moon.pharm.profile.settings.viewmodel

import com.moon.pharm.profile.settings.model.LocationPermission
import com.moon.pharm.profile.settings.model.NotificationPermission
import com.moon.pharm.profile.settings.model.PermissionSettings
import com.moon.pharm.profile.settings.repository.PermissionSettingsRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SettingsViewModelTest {

    @Test
    fun `시스템 설정 복귀 후 최신 권한 상태를 반영한다`() {
        val repository = FakePermissionSettingsRepository()
        val viewModel = SettingsViewModel(repository)

        repository.currentPermissionSettings = repository.currentPermissionSettings.copy(
            locationPermission = LocationPermission.Precise,
            appNotification = NotificationPermission.Enabled,
            medicationNotification = NotificationPermission.Enabled,
            consultNotification = NotificationPermission.Enabled
        )
        viewModel.refresh()

        with(viewModel.uiState.value.permissionSettings) {
            assertEquals(LocationPermission.Precise, locationPermission)
            assertEquals(NotificationPermission.Enabled, appNotification)
            assertFalse(requiresSystemSettings)
        }
    }

    @Test
    fun `허용되지 않은 권한이 있으면 시스템 설정 이동을 노출한다`() {
        val viewModel = SettingsViewModel(FakePermissionSettingsRepository())

        assertTrue(viewModel.uiState.value.permissionSettings.requiresSystemSettings)
    }

    private class FakePermissionSettingsRepository : PermissionSettingsRepository {
        var currentPermissionSettings = PermissionSettings(
            locationPermission = LocationPermission.NotGranted,
            appNotification = NotificationPermission.Disabled,
            medicationNotification = NotificationPermission.Disabled,
            consultNotification = NotificationPermission.Disabled
        )

        override fun getPermissionSettings(): PermissionSettings = currentPermissionSettings
    }
}
