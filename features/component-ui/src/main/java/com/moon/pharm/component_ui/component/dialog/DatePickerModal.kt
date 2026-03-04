package com.moon.pharm.component_ui.component.dialog

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.R
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews

@Composable
fun DatePickerModal(
    state: DatePickerState,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(state.selectedDateMillis)
                onDismiss()
            }) {
                Text(
                    stringResource(R.string.common_confirm),
                    color = PharmTheme.colors.primary, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    stringResource(R.string.common_cancel),
                    color = PharmTheme.colors.primary)
            }
        },
        shape = RoundedCornerShape(10.dp),
        colors = DatePickerDefaults.colors(containerColor = PharmTheme.colors.tertiaryContainer)
    ) {
        DatePicker(
            state = state,
            colors = DatePickerDefaults.colors(
                containerColor = PharmTheme.colors.surface,
                titleContentColor = PharmTheme.colors.onSurface,
                headlineContentColor = PharmTheme.colors.onSurface,
                selectedDayContainerColor = PharmTheme.colors.primary,
                selectedDayContentColor = PharmTheme.colors.surface,
                todayContentColor = PharmTheme.colors.primary,
                todayDateBorderColor = PharmTheme.colors.primary
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@ThemePreviews
@Composable
private fun DatePickerModalPreview() {
    PharmMasterTheme {
        val state = rememberDatePickerState()
        DatePickerModal(
            state = state,
            onDateSelected = {},
            onDismiss = {}
        )
    }
}