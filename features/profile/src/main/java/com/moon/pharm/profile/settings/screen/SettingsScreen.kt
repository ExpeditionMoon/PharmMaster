package com.moon.pharm.profile.settings.screen

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.moon.pharm.designsystem.component.bar.PharmTopBar
import com.moon.pharm.designsystem.model.TopBarData
import com.moon.pharm.designsystem.model.TopBarNavigationType
import com.moon.pharm.designsystem.theme.PharmMasterTheme
import com.moon.pharm.designsystem.theme.PharmTheme
import com.moon.pharm.designsystem.util.ThemePreviews
import com.moon.pharm.profile.R
import com.moon.pharm.profile.settings.model.LocationPermission
import com.moon.pharm.profile.settings.model.NotificationPermission
import com.moon.pharm.profile.settings.model.PermissionSettings
import com.moon.pharm.profile.settings.viewmodel.SettingsUiState
import com.moon.pharm.profile.settings.viewmodel.SettingsViewModel

@Composable
fun SettingsRoute(
    onNavigateUp: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) viewModel.refresh()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    SettingsScreen(
        uiState = uiState,
        onNavigateUp = onNavigateUp,
        onOpenSystemSettings = {
            context.startActivity(
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
            )
        }
    )
}

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onNavigateUp: () -> Unit,
    onOpenSystemSettings: () -> Unit
) {
    val permissionSettings = uiState.permissionSettings

    Scaffold(
        topBar = {
            PharmTopBar(
                data = TopBarData(
                    title = stringResource(R.string.settings_title),
                    navigationType = TopBarNavigationType.Back,
                    onNavigationClick = onNavigateUp
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SettingsSection(
                title = stringResource(R.string.settings_location_section_title),
                icon = Icons.Outlined.LocationOn
            ) {
                SettingStatusRow(
                    title = stringResource(R.string.settings_location_title),
                    description = stringResource(R.string.settings_location_description),
                    status = permissionSettings.locationPermission.toText()
                )
            }

            SettingsSection(
                title = stringResource(R.string.settings_notification_section_title),
                icon = Icons.Outlined.Notifications
            ) {
                SettingStatusRow(
                    title = stringResource(R.string.settings_app_notification_title),
                    description = stringResource(R.string.settings_app_notification_description),
                    status = permissionSettings.appNotification.toText()
                )
                HorizontalDivider(color = PharmTheme.colors.background)
                SettingStatusRow(
                    title = stringResource(R.string.settings_medication_notification_title),
                    description = stringResource(R.string.settings_medication_notification_description),
                    status = permissionSettings.medicationNotification.toText()
                )
                HorizontalDivider(color = PharmTheme.colors.background)
                SettingStatusRow(
                    title = stringResource(R.string.settings_consult_notification_title),
                    description = stringResource(R.string.settings_consult_notification_description),
                    status = permissionSettings.consultNotification.toText()
                )
            }

            if (permissionSettings.requiresSystemSettings) {
                SystemSettingsNotice(onOpenSystemSettings = onOpenSystemSettings)
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Column {
        androidx.compose.foundation.layout.Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PharmTheme.colors.primary
            )
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = PharmTheme.colors.secondFont
            )
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = PharmTheme.colors.surface)
        ) {
            content()
        }
    }
}

@Composable
private fun SettingStatusRow(
    title: String,
    description: String,
    status: String
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = PharmTheme.colors.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                fontSize = 13.sp,
                color = PharmTheme.colors.secondFont
            )
        }
        Text(
            text = status,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = PharmTheme.colors.primary
        )
    }
}

@Composable
private fun SystemSettingsNotice(onOpenSystemSettings: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PharmTheme.colors.surface, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.settings_system_notice_title),
            fontWeight = FontWeight.SemiBold,
            color = PharmTheme.colors.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.settings_system_notice_description),
            fontSize = 13.sp,
            color = PharmTheme.colors.secondFont
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = onOpenSystemSettings,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PharmTheme.colors.primary)
        ) {
            Text(stringResource(R.string.settings_open_system_settings))
        }
    }
}

@Composable
private fun LocationPermission.toText(): String = stringResource(
    when (this) {
        LocationPermission.Precise -> R.string.settings_status_precise_location
        LocationPermission.Approximate -> R.string.settings_status_approximate_location
        LocationPermission.NotGranted -> R.string.settings_status_not_allowed
    }
)

@Composable
private fun NotificationPermission.toText(): String = stringResource(
    when (this) {
        NotificationPermission.Enabled -> R.string.settings_status_allowed
        NotificationPermission.Disabled -> R.string.settings_status_not_allowed
    }
)

@ThemePreviews
@Composable
private fun SettingsScreenPreview() {
    PharmMasterTheme {
        SettingsScreen(
            uiState = SettingsUiState(
                permissionSettings = PermissionSettings(
                    locationPermission = LocationPermission.Approximate,
                    appNotification = NotificationPermission.Enabled,
                    medicationNotification = NotificationPermission.Enabled,
                    consultNotification = NotificationPermission.Disabled
                )
            ),
            onNavigateUp = {},
            onOpenSystemSettings = {}
        )
    }
}
