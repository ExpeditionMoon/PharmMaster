package com.moon.pharm.profile.medication.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.component.chip.FilterChip
import com.moon.pharm.domain.model.MealTiming

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
