package com.moon.pharm.profile.medication.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.component.button.SelectButton
import com.moon.pharm.domain.model.RepeatType

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
