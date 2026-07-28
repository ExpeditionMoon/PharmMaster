package com.moon.pharm.profile.medication.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moon.pharm.designsystem.component.button.SelectButton
import com.moon.pharm.designsystem.theme.PharmMasterTheme
import com.moon.pharm.designsystem.util.ThemePreviews
import com.moon.pharm.profile.medication.model.RepeatTypeUiModel

@Composable
fun AlarmOptionSelector(
    selectedOption: RepeatTypeUiModel,
    onOptionSelected: (RepeatTypeUiModel) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RepeatTypeUiModel.entries.forEach { type ->
            SelectButton(
                text = stringResource(type.labelRes),
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
                selectedOption = RepeatTypeUiModel.Daily,
                onOptionSelected = {}
            )
        }
    }
}
