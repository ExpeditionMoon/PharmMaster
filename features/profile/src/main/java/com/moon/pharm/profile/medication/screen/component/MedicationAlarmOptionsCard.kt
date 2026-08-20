package com.moon.pharm.profile.medication.screen.component

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.moon.pharm.designsystem.theme.PharmMasterTheme
import com.moon.pharm.designsystem.theme.PharmTheme
import com.moon.pharm.designsystem.util.ThemePreviews
import com.moon.pharm.profile.R
import com.moon.pharm.profile.medication.viewmodel.MedicationFormState
import com.moon.pharm.profile.medication.viewmodel.MedicationUiEvent

private const val PERMISSION_PREFERENCES_NAME = "medication_alarm_permissions"
private const val KEY_NOTIFICATION_PERMISSION_REQUESTED = "notification_permission_requested"

@Composable
fun MedicationAlarmOptionsCard(
    medicationIndex: Int,
    form: MedicationFormState,
    onEvent: (MedicationUiEvent) -> Unit
) {
    val context = LocalContext.current
    var permissionStateVersion by remember { mutableIntStateOf(0) }
    val shouldOpenNotificationSettings = remember(context, permissionStateVersion) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.shouldOpenNotificationSettings()
        } else {
            false
        }
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        permissionStateVersion++
    }
    val isNotificationPermissionGranted = remember(context, permissionStateVersion) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.isNotificationPermissionGranted()
        } else {
            true
        }
    }
    val isExactAlarmPermissionGranted = remember(context, permissionStateVersion) {
        Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
            context.getSystemService<AlarmManager>()?.canScheduleExactAlarms() == true
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) permissionStateVersion++
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(form.isAlarmEnabled, isNotificationPermissionGranted, shouldOpenNotificationSettings) {
        if (form.isAlarmEnabled &&
            !isNotificationPermissionGranted &&
            !shouldOpenNotificationSettings &&
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
        ) {
            requestNotificationPermission(context, notificationPermissionLauncher)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(PharmTheme.colors.surface, RoundedCornerShape(12.dp))
            .border(0.5.dp, PharmTheme.colors.tertiary, RoundedCornerShape(12.dp))
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column {
            MedicationSwitchRow(
                title = stringResource(R.string.medication_alarm_enabled_title),
                description = stringResource(R.string.medication_alarm_enabled_desc),
                isChecked = form.isAlarmEnabled,
                onCheckedChange = { isEnabled ->
                    onEvent(MedicationUiEvent.UpdateAlarmEnabled(medicationIndex, isEnabled))
                    if (isEnabled && !isNotificationPermissionGranted &&
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                    ) {
                        if (shouldOpenNotificationSettings) {
                            context.openAppNotificationSettings()
                        } else {
                            requestNotificationPermission(context, notificationPermissionLauncher)
                        }
                    }
                },
                showInfoIcon = false
            )
            if (form.isAlarmEnabled) {
                AlarmPermissionActions(
                    isNotificationPermissionGranted = isNotificationPermissionGranted,
                    isExactAlarmPermissionGranted = isExactAlarmPermissionGranted,
                    onRequestNotificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        {
                            if (shouldOpenNotificationSettings) {
                                context.openAppNotificationSettings()
                            } else {
                                requestNotificationPermission(context, notificationPermissionLauncher)
                            }
                        }
                    } else {
                        null
                    },
                    notificationPermissionActionLabel = stringResource(
                        if (shouldOpenNotificationSettings) {
                            R.string.medication_alarm_notification_settings_action
                        } else {
                            R.string.medication_alarm_notification_permission_action
                        }
                    ),
                    onOpenExactAlarmSettings = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        { context.openExactAlarmSettings() }
                    } else {
                        null
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = PharmTheme.colors.background)
            Spacer(modifier = Modifier.height(8.dp))
            MedicationSwitchRow(
                title = stringResource(R.string.medication_alarm_grouped_title),
                description = stringResource(R.string.medication_alarm_grouped_desc),
                isChecked = form.isGrouped,
                onCheckedChange = { onEvent(MedicationUiEvent.UpdateGroupedNotification(index = medicationIndex, enabled = it)) },
                explanation = stringResource(R.string.medication_alarm_group_description)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun requestNotificationPermission(
    context: Context,
    launcher: ActivityResultLauncher<String>
) {
    context.getSharedPreferences(PERMISSION_PREFERENCES_NAME, Context.MODE_PRIVATE)
        .edit { putBoolean(KEY_NOTIFICATION_PERMISSION_REQUESTED, true) }
    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun Context.isNotificationPermissionGranted(): Boolean =
    ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun Context.shouldOpenNotificationSettings(): Boolean {
    val hasRequestedPermission = getSharedPreferences(
        PERMISSION_PREFERENCES_NAME,
        Context.MODE_PRIVATE
    ).getBoolean(KEY_NOTIFICATION_PERMISSION_REQUESTED, false)
    val canRequestPermissionAgain = findActivity()?.let { activity ->
        ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            Manifest.permission.POST_NOTIFICATIONS
        )
    } == true

    return hasRequestedPermission && !canRequestPermissionAgain
}

private fun Context.openAppNotificationSettings() {
    startActivity(
        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).putExtra(
            Settings.EXTRA_APP_PACKAGE,
            packageName
        )
    )
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@RequiresApi(Build.VERSION_CODES.S)
private fun Context.openExactAlarmSettings() {
    startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
        data = "package:$packageName".toUri()
    })
}

@Composable
private fun AlarmPermissionActions(
    isNotificationPermissionGranted: Boolean,
    isExactAlarmPermissionGranted: Boolean,
    onRequestNotificationPermission: (() -> Unit)?,
    notificationPermissionActionLabel: String,
    onOpenExactAlarmSettings: (() -> Unit)?
) {
    Column {
        if (!isNotificationPermissionGranted) {
            PermissionNotice(
                title = stringResource(R.string.medication_alarm_notification_permission_title),
                description = stringResource(R.string.medication_alarm_notification_permission_desc),
                actionLabel = notificationPermissionActionLabel,
                actionIcon = Icons.Outlined.Notifications,
                onAction = onRequestNotificationPermission
            )
        }

        if (!isExactAlarmPermissionGranted) {
            if (!isNotificationPermissionGranted) Spacer(modifier = Modifier.height(8.dp))
            PermissionNotice(
                title = stringResource(R.string.medication_alarm_exact_permission_title),
                description = stringResource(R.string.medication_alarm_exact_permission_desc),
                actionLabel = stringResource(R.string.medication_alarm_exact_permission_action),
                actionIcon = Icons.Outlined.AccessTime,
                onAction = onOpenExactAlarmSettings
            )
        }
    }
}

@Composable
private fun PermissionNotice(
    title: String,
    description: String,
    actionLabel: String,
    actionIcon: ImageVector,
    onAction: (() -> Unit)?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PharmTheme.colors.surface, RoundedCornerShape(10.dp))
            .border(1.dp, PharmTheme.colors.tertiary, RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Text(
            text = title,
            color = PharmTheme.colors.onInfoContainer,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            color = PharmTheme.colors.secondFont,
            fontSize = 12.sp
        )
        onAction?.let { action ->
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = action,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, PharmTheme.colors.primary),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = PharmTheme.colors.surface,
                    contentColor = PharmTheme.colors.primary
                )
            ) {
                Icon(imageVector = actionIcon, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = actionLabel, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@ThemePreviews
@Composable
private fun MedicationAlarmOptionsCardPreview() {
    PharmMasterTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            MedicationAlarmOptionsCard(
                medicationIndex = 0,
                form = MedicationFormState(isGrouped = true),
                onEvent = {}
            )
        }
    }
}
