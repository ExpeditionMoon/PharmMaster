package com.moon.pharm.profile.medication.screen.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.component.card.TimeSettingCard
import com.moon.pharm.component_ui.component.dialog.TimePickerDialog
import com.moon.pharm.component_ui.theme.Secondary
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.component_ui.util.toMinuteTimeUiString
import com.moon.pharm.profile.R
import com.moon.pharm.profile.medication.screen.MedicationFormState
import com.moon.pharm.profile.medication.screen.component.AlarmOptionSelector
import com.moon.pharm.profile.medication.screen.component.AlarmTypeSection
import com.moon.pharm.profile.medication.screen.component.MealTimeChips
import com.moon.pharm.profile.medication.viewmodel.MedicationUiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationAlarmSection(
    form: MedicationFormState,
    onEvent: (MedicationUiEvent) -> Unit
) {
    var showTimePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .wrapContentSize()
            .background(backgroundLight)
            .background(Secondary.copy(alpha = 0.1f), RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Text(
            text = stringResource(R.string.medication_setting_alarm),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        MealTimeChips(
            selectedTimes = form.selectedMealTiming,
            onTimeClick = { onEvent(MedicationUiEvent.UpdateMealTiming(it)) }
        )

        Spacer(modifier = Modifier.height(15.dp))


        TimeSettingCard(
            time = form.selectedTime.toMinuteTimeUiString(),
            onTimeClick = { showTimePicker = true }
        )

        if (showTimePicker) {
            TimePickerDialog(
                title = stringResource(id = R.string.medication_alarm_time_dialog_title),
                onConfirm = { state ->
                    onEvent(MedicationUiEvent.UpdateAlarmTime(state.hour, state.minute))
                    showTimePicker = false
                },
                onDismiss = { showTimePicker = false }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        AlarmOptionSelector(
            selectedOption = form.selectedRepeatType,
            onOptionSelected = { onEvent(MedicationUiEvent.UpdateRepeatType(it)) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        AlarmTypeSection(
            isChecked = form.isMealTimeAlarmEnabled,
            onCheckedChange = { onEvent(MedicationUiEvent.UpdateMealAlarm(it)) }
        )
    }
}