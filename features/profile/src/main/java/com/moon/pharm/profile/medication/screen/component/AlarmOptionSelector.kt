package com.moon.pharm.profile.medication.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.component.button.SelectButton
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.util.ThemePreviews
import com.moon.pharm.domain.model.medication.RepeatType

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

@ThemePreviews
@Composable
private fun AlarmOptionSelectorPreview() {
    PharmMasterTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            AlarmOptionSelector(
                selectedOption = RepeatType.DAILY,
                onOptionSelected = {}
            )
        }
    }
}