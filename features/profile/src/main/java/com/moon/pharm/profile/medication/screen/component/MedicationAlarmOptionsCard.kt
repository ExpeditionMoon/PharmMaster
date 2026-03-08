package com.moon.pharm.profile.medication.screen.component

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews
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
                title = stringResource(R.string.medication_alarm_grouped_title),
                description = stringResource(R.string.medication_alarm_grouped_desc),
                isChecked = form.isGrouped,
                onCheckedChange = { onEvent(MedicationUiEvent.UpdateGroupedNotification(index = medicationIndex, enabled = it)) },
                explanation = stringResource(R.string.medication_alarm_group_description)
            )
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
