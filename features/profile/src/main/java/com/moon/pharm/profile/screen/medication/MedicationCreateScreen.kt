package com.moon.pharm.profile.screen.medication

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.moon.pharm.component_ui.theme.Placeholder
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.component_ui.view.PrimaryTextField
import com.moon.pharm.component_ui.view.DateInputBox
import com.moon.pharm.component_ui.view.DatePickerModal
import com.moon.pharm.component_ui.view.FilterChip
import com.moon.pharm.component_ui.view.SelectButton
import com.moon.pharm.component_ui.view.TimePickerDialog
import com.moon.pharm.component_ui.view.TimeSettingCard
import com.moon.pharm.domain.model.MealTiming
import com.moon.pharm.domain.model.RepeatType
import com.moon.pharm.profile.viewmodel.MedicationViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationCreateScreen(
    navController: NavController? = null, viewModel: MedicationViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val form = uiState.form
    var showTimePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLight)
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        MedicationTypeSelector(
            selectedType = form.selectedType,
            onTypeSelected = { viewModel.updateType(it) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "약물 이름 입력",
            fontSize = 14.sp,
            color = SecondFont,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        PrimaryTextField(
            value = form.medicationName,
            onValueChange = { viewModel.updateName(it) },
            placeholder = "약물 이름 입력"
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "복용 용량 (예: 2알, 1캡슐)", fontSize = 14.sp, color = SecondFont)
        PrimaryTextField(
            value = form.medicationDosage,
            onValueChange = { viewModel.updateDosage(it) },
            placeholder = "복용 용량 입력"
        )

        Spacer(modifier = Modifier.height(10.dp))

        PeriodInputSection(
            startDate = form.startDate,
            endDate = form.endDate,
            noEndDate = form.noEndDate,
            onStateChange = { start, end, noEnd ->
                viewModel.updatePeriod(start, end, noEnd)
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "알림 설정",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        MealTimeChips(
            selectedTimes = form.selectedMealTiming,
            onTimeClick = { viewModel.updateMealTiming(it) }
        )

        Spacer(modifier = Modifier.height(15.dp))


        TimeSettingCard(
            time = form.selectedTime.toDisplayString(),
            onTimeClick = { showTimePicker = true }
        )

        if (showTimePicker) {
            TimePickerDialog(
                onConfirm = { state ->
                    viewModel.updateAlarmTime(state.hour, state.minute)
                    showTimePicker = false
                },
                onDismiss = { showTimePicker = false }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        AlarmOptionSelector(
            selectedOption = form.selectedRepeatType,
            onOptionSelected = { viewModel.updateRepeatType(it) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        AlarmTypeSection(
            isChecked = form.isMealTimeAlarmEnabled,
            onCheckedChange = { viewModel.updateMealTimeAlarmEnabled(it) }
        )

        Spacer(modifier = Modifier.height(50.dp))

        SelectButton(
            text = "등록하기",
            isSelected = form.medicationName.isNotEmpty(),
            onClick = { viewModel.saveMedication() },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@SuppressLint("NewApi")
fun LocalDate?.toDisplayDateString(): String {
    return this?.format(java.time.format.DateTimeFormatter.ofPattern("yyyy.MM.dd")) ?: ""
}

@SuppressLint("NewApi")
fun Long?.toLocalDate(): java.time.LocalDate {
    val millis = this ?: System.currentTimeMillis()
    return java.time.Instant.ofEpochMilli(millis)
        .atZone(java.time.ZoneId.systemDefault())
        .toLocalDate()
}
@Composable
fun PeriodInputSection(
    startDate: LocalDate?,
    endDate: LocalDate?,
    noEndDate: Boolean,
    onStateChange: (LocalDate?, LocalDate?, Boolean) -> Unit
) {
    val startDatePickerState = rememberDatePickerState()
    val endDatePickerState = rememberDatePickerState()

    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        DateInputBox(
            label = "시작일",
            value = startDate.toDisplayDateString(),
            showCalendarIcon = true,
            onClick = { showStartPicker = true },
            modifier = Modifier.weight(1f)
        )
        if (!noEndDate) {
            DateInputBox(
                label = "종료일",
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
                onStateChange(millis.toLocalDate(), endDate, noEndDate)
                showStartPicker = false
            },
            onDismiss = { showStartPicker = false }
        )
    }

    if (showEndPicker) {
        DatePickerModal(
            state = endDatePickerState,
            onDateSelected = { millis ->
                onStateChange(startDate, millis.toLocalDate(), noEndDate)
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
            text = "종료일 없음",
            fontSize = 12.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.width(4.dp))
        Switch(
            checked = noEndDate,
            onCheckedChange = { checked ->
                onStateChange(startDate, if (checked) null else endDate, checked)
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

@SuppressLint("NewApi")
private fun LocalTime?.toDisplayString(): String {
    return this?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "시간 선택"
}