package com.moon.pharm.component_ui.component.toggle

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import com.moon.pharm.component_ui.theme.PharmTheme

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