package com.moon.pharm.profile.medication.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moon.pharm.designsystem.component.chip.FilterChip
import com.moon.pharm.designsystem.theme.PharmMasterTheme
import com.moon.pharm.designsystem.util.ThemePreviews
import com.moon.pharm.profile.medication.model.MealTimingUiModel

@Composable
fun MealTimeChips(
    selectedTimes: MealTimingUiModel,
    onTimeClick: (MealTimingUiModel) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(MealTimingUiModel.entries.toTypedArray()) { timing ->
            FilterChip(
                text = stringResource(timing.labelRes),
                isSelected = selectedTimes == timing,
                onClick = { onTimeClick(timing) }
            )
        }
    }
}

@ThemePreviews
@Composable
private fun MealTimeChipsPreview() {
    PharmMasterTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            MealTimeChips(
                selectedTimes = MealTimingUiModel.AfterMeal,
                onTimeClick = {}
            )
        }
    }
}
