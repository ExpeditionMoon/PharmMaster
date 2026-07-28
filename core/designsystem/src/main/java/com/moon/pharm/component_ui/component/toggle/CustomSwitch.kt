package com.moon.pharm.component_ui.component.toggle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews

@Composable
fun CustomSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = PharmTheme.colors.surface,
            checkedTrackColor = PharmTheme.colors.primary,
            uncheckedThumbColor = PharmTheme.colors.surface,
            uncheckedTrackColor = PharmTheme.colors.placeholder,
            uncheckedBorderColor = Color.Transparent
        ),
        modifier = modifier.scale(0.7f)
    )
}

@ThemePreviews
@Composable
private fun CustomSwitchPreview() {
    PharmMasterTheme {
        Row(
            modifier = Modifier
                .background(PharmTheme.colors.background)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomSwitch(checked = true, onCheckedChange = {})
            CustomSwitch(checked = false, onCheckedChange = {})
        }
    }
}