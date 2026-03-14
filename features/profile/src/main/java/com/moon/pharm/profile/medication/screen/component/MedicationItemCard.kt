package com.moon.pharm.profile.medication.screen.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.component.item.PharmListItem
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.util.ThemePreviews
import com.moon.pharm.domain.model.medication.MealTiming
import com.moon.pharm.domain.model.medication.MedicationType
import com.moon.pharm.domain.model.medication.RepeatType
import com.moon.pharm.domain.model.medication.TodayMedicationUiModel

@Composable
fun MedicationItemCard(
    item: TodayMedicationUiModel,
    onTakeClick: (TodayMedicationUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    PharmListItem(
        modifier = modifier,
        headline = "${item.name} · ${item.type.label}",
        subhead = "${item.mealTiming.label} · ${item.repeatType.label}",
        trailing = {
            MedicationCheckButton(
                isTaken = item.isTaken,
                onClick = { onTakeClick(item) }
            )
        }
    )
}

@ThemePreviews
@Composable
private fun MedicationItemCardPreview() {
    PharmMasterTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            MedicationItemCard(
                item = TodayMedicationUiModel(
                    medicationId = "m1",
                    scheduleId = "s1",
                    name = "혈압약",
                    type = MedicationType.OTC,
                    repeatType = RepeatType.DAILY,
                    time = "오전 08:00",
                    dosage = "1알",
                    mealTiming = MealTiming.AFTER_MEAL,
                    isTaken = false
                ),
                onTakeClick = {}
            )
        }
    }
}