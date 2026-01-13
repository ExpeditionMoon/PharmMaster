package com.moon.pharm.profile.medication.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.theme.Placeholder
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.util.toDisplayDateString
import com.moon.pharm.component_ui.component.card.DateSettingCard
import com.moon.pharm.component_ui.component.dialog.DatePickerModal
import com.moon.pharm.profile.medication.viewmodel.MedicationUiEvent
import com.moon.pharm.profile.R

@Composable
fun PeriodInputSection(
    startDate: Long?,
    endDate: Long?,
    noEndDate: Boolean,
    onEvent: (MedicationUiEvent) -> Unit
) {
    val endDatePickerState = rememberDatePickerState()
    val startDatePickerState = rememberDatePickerState()

    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        DateSettingCard(
            placeholder = stringResource(R.string.medication_start_date),
            value = startDate.toDisplayDateString(),
            showCalendarIcon = true,
            onClick = { showStartPicker = true },
            modifier = Modifier.weight(1f)
        )
        if (!noEndDate) {
            DateSettingCard(
                placeholder = stringResource(R.string.medication_end_date),
                value = endDate.toDisplayDateString(),
                showCalendarIcon = true,
                onClick = { showEndPicker = true },
                modifier = Modifier.weight(1f)
            )
        }
    }

    if (showStartPicker) {
        DatePickerModal(
            state = startDatePickerState,
            onDateSelected = { millis ->
                onEvent(MedicationUiEvent.UpdateStartDate(millis))
                showStartPicker = false
            },
            onDismiss = { showStartPicker = false }
        )
    }

    if (showEndPicker) {
        DatePickerModal(
            state = endDatePickerState,
            onDateSelected = { millis ->
                onEvent(MedicationUiEvent.UpdateEndDate(millis))
                showEndPicker = false
            },
            onDismiss = { showEndPicker = false }
        )
    }

    Spacer(modifier = Modifier.height(10.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.medication_no_end_date),
            fontSize = 12.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.width(4.dp))
        Switch(
            checked = noEndDate,
            onCheckedChange = { checked ->
                onEvent(MedicationUiEvent.UpdatePeriod(startDate, if (checked) null else endDate, checked))
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = White,
                checkedTrackColor = Primary,
                uncheckedThumbColor = White,
                uncheckedTrackColor = Placeholder,
                uncheckedBorderColor = Color.Transparent
            ),
            modifier = Modifier.scale(0.7f)
        )
    }
}
