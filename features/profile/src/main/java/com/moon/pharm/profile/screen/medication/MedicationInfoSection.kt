package com.moon.pharm.profile.screen.medication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.moon.pharm.component_ui.theme.Placeholder
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.Secondary
import com.moon.pharm.component_ui.theme.Tertiary
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.component_ui.util.toDisplayDateString
import com.moon.pharm.component_ui.view.DateInputBox
import com.moon.pharm.component_ui.view.DatePickerModal
import com.moon.pharm.component_ui.view.PrimaryTextField
import com.moon.pharm.domain.model.MedicationType
import com.moon.pharm.profile.viewmodel.MedicationIntent
import java.time.LocalDate

@Composable
fun MedicationInfoSection(
    form: MedicationForm,
    onIntent: (MedicationIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .background(backgroundLight)
            .background(Secondary.copy(alpha = 0.1f), RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        MedicationTypeSelector(
            selectedType = form.selectedType,
            onTypeSelected = { onIntent(MedicationIntent.UpdateType(it)) }
        )
        Spacer(modifier = Modifier.height(10.dp))

        PrimaryTextField(
            value = form.medicationName,
            onValueChange = { onIntent(MedicationIntent.UpdateName(it)) },
            placeholder = "약물 이름 입력"
        )

        Spacer(modifier = Modifier.height(10.dp))

        PrimaryTextField(
            value = form.medicationDosage,
            onValueChange = { onIntent(MedicationIntent.UpdateDosage(it)) },
            placeholder = "복용 용량 입력(예: 2알, 1캡슐)"
        )

        Spacer(modifier = Modifier.height(10.dp))

        PeriodInputSection(
            startDate = form.startDate,
            endDate = form.endDate,
            noEndDate = form.noEndDate,
            onIntent = onIntent
        )
    }
}

@Composable
fun MedicationTypeSelector(
    selectedType: MedicationType,
    onTypeSelected: (MedicationType) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MedicationType.entries.forEach { type ->
            val isSelected = selectedType == type
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(32.dp)
                    .background(
                        color = if (isSelected) Primary else Tertiary,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .clickable { onTypeSelected(type) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = type.label,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) White else SecondFont
                )
            }
        }
    }
}

@Composable
fun PeriodInputSection(
    startDate: LocalDate?,
    endDate: LocalDate?,
    noEndDate: Boolean,
    onIntent: (MedicationIntent) -> Unit
) {
    val endDatePickerState = rememberDatePickerState()
    val startDatePickerState = rememberDatePickerState()

    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        DateInputBox(
            placeholder = "시작일",
            value = startDate.toDisplayDateString(),
            showCalendarIcon = true,
            onClick = { showStartPicker = true },
            modifier = Modifier.weight(1f)
        )
        if (!noEndDate) {
            DateInputBox(
                placeholder = "종료일",
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
                onIntent(MedicationIntent.UpdateStartDate(millis))
                showStartPicker = false
            },
            onDismiss = { showStartPicker = false }
        )
    }

    if (showEndPicker) {
        DatePickerModal(
            state = endDatePickerState,
            onDateSelected = { millis ->
                onIntent(MedicationIntent.UpdateEndDate(millis))
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
                onIntent(MedicationIntent.UpdatePeriod(startDate, if (checked) null else endDate, checked))
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
