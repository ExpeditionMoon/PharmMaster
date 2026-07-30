package com.moon.pharm.profile.medication.screen.component

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moon.pharm.designsystem.theme.PharmMasterTheme
import com.moon.pharm.designsystem.theme.PharmTheme
import com.moon.pharm.designsystem.util.ThemePreviews
import com.moon.pharm.profile.R
import com.moon.pharm.profile.medication.viewmodel.MedicationFormState
import com.moon.pharm.profile.medication.viewmodel.MedicationUiEvent

@Composable
fun MedicationAlarmOptionsCard(
    medicationIndex: Int,
    form: MedicationFormState,
    onEvent: (MedicationUiEvent) -> Unit
) {
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
                onCheckedChange = { onEvent(MedicationUiEvent.UpdateAlarmEnabled(medicationIndex, it)) },
                explanation = stringResource(R.string.medication_alarm_exact_permission_desc)
            )
            if (form.isAlarmEnabled) ExactAlarmPermissionAction()
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

@Composable
private fun ExactAlarmPermissionAction() {
    val context = LocalContext.current
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val needsExactAlarmPermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
        !alarmManager.canScheduleExactAlarms()

    if (needsExactAlarmPermission) {
        androidx.compose.material3.TextButton(
            onClick = {
                context.startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    data = android.net.Uri.parse("package:${context.packageName}")
                })
            }
        ) {
            androidx.compose.material3.Text(stringResource(R.string.medication_alarm_exact_permission_action))
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
