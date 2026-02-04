package com.moon.pharm.profile.medication.screen.section

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.theme.Black
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.Success
import com.moon.pharm.component_ui.theme.Warning
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.util.toDisplayString
import com.moon.pharm.component_ui.util.toQueryString
import com.moon.pharm.profile.medication.model.HistoryRecordUiModel
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun HistoryCalendarSection(
    currentMonth: YearMonth,
    recordsByDate: Map<String, List<HistoryRecordUiModel>>,
    selectedDate: LocalDate,
    onMonthChanged: (YearMonth) -> Unit,
    onDateClick: (LocalDate) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        CalendarHeader(
            currentMonth = currentMonth,
            onMonthChanged = onMonthChanged
        )

        Spacer(modifier = Modifier.height(16.dp))

        DayOfWeekHeader()

        Spacer(modifier = Modifier.height(8.dp))

        CalendarGrid(
            currentMonth = currentMonth,
            selectedDate = selectedDate,
            recordsByDate = recordsByDate,
            onDateClick = onDateClick
        )
    }
}

@Composable
private fun CalendarHeader(
    currentMonth: YearMonth,
    onMonthChanged: (YearMonth) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onMonthChanged(currentMonth.minusMonths(1)) }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "이전 달",
                tint = Black
            )
        }

        Text(
            text = currentMonth.toDisplayString(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Black
        )

        IconButton(onClick = { onMonthChanged(currentMonth.plusMonths(1)) }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "다음 달",
                tint = Black
            )
        }
    }
}

@Composable
private fun DayOfWeekHeader() {
    val daysOfWeek = listOf("일", "월", "화", "수", "목", "금", "토")
    Row(modifier = Modifier.fillMaxWidth()) {
        daysOfWeek.forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                color = SecondFont,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun CalendarGrid(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    recordsByDate: Map<String, List<HistoryRecordUiModel>>,
    onDateClick: (LocalDate) -> Unit
) {
    val firstDayOfMonth = currentMonth.atDay(1)
    val daysInMonth = currentMonth.lengthOfMonth()
    val startDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth().height(300.dp),
        userScrollEnabled = false
    ) {
        items(startDayOfWeek) {
            Box(modifier = Modifier.aspectRatio(1f))
        }

        items(daysInMonth) { dayOffset ->
            val day = dayOffset + 1
            val date = currentMonth.atDay(day)
            val isSelected = date == selectedDate

            val queryKey = date.toQueryString()
            val dailyRecords = recordsByDate[queryKey] ?: emptyList()

            DayCell(
                day = day,
                isSelected = isSelected,
                dailyRecords = dailyRecords,
                onClick = { onDateClick(date) }
            )
        }
    }
}

@Composable
private fun DayCell(
    day: Int,
    isSelected: Boolean,
    dailyRecords: List<HistoryRecordUiModel>,
    onClick: () -> Unit
) {
    val dotColor = when {
        dailyRecords.isEmpty() -> Color.Transparent
        dailyRecords.all { it.record.isTaken } -> Success
        dailyRecords.any { !it.record.isTaken } -> Warning
        else -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .clip(CircleShape)
            .background(if (isSelected) Primary else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = day.toString(),
                color = if (isSelected) White else Black,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 14.sp
            )

            if (dailyRecords.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) White else dotColor)
                )
            }
        }
    }
}