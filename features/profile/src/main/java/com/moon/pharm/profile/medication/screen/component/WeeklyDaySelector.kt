package com.moon.pharm.profile.medication.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moon.pharm.profile.medication.model.WeekdayUiModel

@Composable
fun WeeklyDaySelector(
    selectedDays: Set<Int>,
    onDayToggle: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        WeekdayUiModel.entries.forEach { day ->
            FilterChip(
                selected = day.value in selectedDays,
                onClick = { onDayToggle(day.value) },
                label = { Text(stringResource(day.labelRes)) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}
