package com.moon.pharm.profile.screen.medication

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.theme.Placeholder
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.Secondary
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.component_ui.theme.tertiaryLight
import com.moon.pharm.component_ui.view.FilterChip
import com.moon.pharm.component_ui.view.SelectButton
import com.moon.pharm.component_ui.view.TimePickerDialog
import com.moon.pharm.component_ui.view.TimeSettingCard
import com.moon.pharm.domain.model.MealTiming
import com.moon.pharm.domain.model.RepeatType
import com.moon.pharm.profile.viewmodel.MedicationIntent
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationAlarmSection(
    form: MedicationForm,
    onIntent: (MedicationIntent) -> Unit
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
            text = "알림 설정",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        MealTimeChips(
            selectedTimes = form.selectedMealTiming,
            onTimeClick = { onIntent(MedicationIntent.UpdateMealTiming(it)) }
        )

        Spacer(modifier = Modifier.height(15.dp))


        TimeSettingCard(
            time = form.selectedTime.toDisplayString(),
            onTimeClick = { showTimePicker = true }
        )

        if (showTimePicker) {
            TimePickerDialog(
                onConfirm = { state ->
                    onIntent(MedicationIntent.UpdateAlarmTime(state.hour, state.minute))
                    showTimePicker = false
                },
                onDismiss = { showTimePicker = false }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        AlarmOptionSelector(
            selectedOption = form.selectedRepeatType,
            onOptionSelected = { onIntent(MedicationIntent.UpdateRepeatType(it)) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        AlarmTypeSection(
            isChecked = form.isMealTimeAlarmEnabled,
            onCheckedChange = { onIntent(MedicationIntent.UpdateMealAlarm(it)) }
        )
    }
}

@Composable
fun MealTimeChips(
    selectedTimes: MealTiming,
    onTimeClick: (MealTiming) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(MealTiming.entries.toTypedArray()) { timing ->
            FilterChip(
                text = timing.label,
                isSelected = selectedTimes == timing,
                onClick = { onTimeClick(timing) }
            )
        }
    }
}

@Composable
fun AlarmOptionSelector(
    selectedOption: RepeatType,
    onOptionSelected: (RepeatType) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RepeatType.entries.forEach { type ->
            SelectButton(
                text = type.label,
                isSelected = selectedOption == type,
                modifier = Modifier.weight(1f),
                onClick = { onOptionSelected(type) }
            )
        }
    }
}

@Composable
fun AlarmTypeSection(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(White, RoundedCornerShape(12.dp))
            .border(0.5.dp, tertiaryLight, RoundedCornerShape(12.dp))
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "묶음 알림 (식사 시간에 따라)",
                fontSize = 14.sp,
                color = Color.Black
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "정보",
                    tint = SecondFont,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = isChecked,
                    onCheckedChange = onCheckedChange,
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
    }
}

@SuppressLint("NewApi")
private fun LocalTime?.toDisplayString(): String {
    return this?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "시간 선택"
}