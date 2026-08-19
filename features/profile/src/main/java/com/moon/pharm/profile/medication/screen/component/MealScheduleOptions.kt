package com.moon.pharm.profile.medication.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moon.pharm.designsystem.component.chip.FilterChip
import com.moon.pharm.profile.medication.model.MealIntervalUiModel
import com.moon.pharm.profile.medication.model.MealSlotUiModel
import com.moon.pharm.profile.medication.model.MedicationScheduleBasisUiModel

@Composable
fun MealScheduleOptions(
    basis: MedicationScheduleBasisUiModel,
    selectedMealSlots: Set<MealSlotUiModel>,
    interval: MealIntervalUiModel,
    onBasisSelected: (MedicationScheduleBasisUiModel) -> Unit,
    onMealSlotToggled: (MealSlotUiModel) -> Unit,
    onIntervalSelected: (MealIntervalUiModel) -> Unit
) {
    Column {
        ScheduleChipRow(
            values = MedicationScheduleBasisUiModel.entries.toList(),
            selected = basis,
            label = MedicationScheduleBasisUiModel::labelRes,
            onSelected = onBasisSelected
        )

        if (basis.isMealBased()) {
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(MealSlotUiModel.entries) { slot ->
                    FilterChip(
                        text = stringResource(slot.labelRes),
                        isSelected = slot in selectedMealSlots,
                        onClick = { onMealSlotToggled(slot) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            ScheduleChipRow(
                values = MealIntervalUiModel.entries.toList(),
                selected = interval,
                label = MealIntervalUiModel::labelRes,
                onSelected = onIntervalSelected
            )
        }
    }
}

@Composable
private fun <T> ScheduleChipRow(
    values: List<T>,
    selected: T,
    label: (T) -> Int,
    onSelected: (T) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(values) { value ->
            FilterChip(
                text = stringResource(label(value)),
                isSelected = value == selected,
                onClick = { onSelected(value) }
            )
        }
    }
}

private fun MedicationScheduleBasisUiModel.isMealBased(): Boolean =
    this == MedicationScheduleBasisUiModel.BeforeMeal || this == MedicationScheduleBasisUiModel.AfterMeal
