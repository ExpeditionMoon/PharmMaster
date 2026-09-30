package com.moon.pharm.profile.settings.viewmodel

import androidx.lifecycle.ViewModel
import com.moon.pharm.profile.settings.model.LocationPermission
import com.moon.pharm.profile.settings.model.NotificationPermission
import com.moon.pharm.profile.settings.model.PermissionSettings
import com.moon.pharm.profile.settings.repository.PermissionSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val permissionSettingsRepository: PermissionSettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        _uiState.value = SettingsUiState(permissionSettingsRepository.getPermissionSettings())
    }
}

data class SettingsUiState(
    val permissionSettings: PermissionSettings = PermissionSettings(
        locationPermission = LocationPermission.NotGranted,
        appNotification = NotificationPermission.Disabled,
        medicationNotification = NotificationPermission.Disabled,
        consultNotification = NotificationPermission.Disabled
    )
)
