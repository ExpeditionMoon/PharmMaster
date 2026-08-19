package com.moon.pharm.profile.medication.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moon.pharm.designsystem.component.chip.FilterChip
import com.moon.pharm.profile.R
import com.moon.pharm.profile.medication.model.MedicationIntakeGroupOptionUiModel

@Composable
fun ExistingIntakeGroupSelector(
    groups: List<MedicationIntakeGroupOptionUiModel>,
    selectedGroupId: String?,
    onGroupSelected: (MedicationIntakeGroupOptionUiModel) -> Unit
) {
    if (groups.isEmpty()) return

    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(groups, key = MedicationIntakeGroupOptionUiModel::id) { group ->
            FilterChip(
                text = stringResource(R.string.medication_group_join_format, group.representativeName),
                isSelected = selectedGroupId == group.id,
                onClick = { onGroupSelected(group) }
            )
        }
    }
}
