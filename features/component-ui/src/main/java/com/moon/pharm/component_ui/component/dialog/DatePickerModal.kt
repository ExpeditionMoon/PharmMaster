package com.moon.pharm.component_ui.component.dialog

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.R
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.theme.tertiaryContainerLight

@Composable
fun DatePickerModal(
    state: DatePickerState,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(state.selectedDateMillis)
                onDismiss()
            }) {
                Text(
                    stringResource(R.string.common_confirm),
                    color = Primary, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    stringResource(R.string.common_cancel),
                    color = Primary)
            }
        },
        shape = RoundedCornerShape(10.dp),
        colors = DatePickerDefaults.colors(containerColor = tertiaryContainerLight)
    ) {
        DatePicker(
            state = state,
            colors = DatePickerDefaults.colors(
                containerColor = White,
                titleContentColor = Color.Black,
                headlineContentColor = Color.Black,
                selectedDayContainerColor = Primary,
                selectedDayContentColor = White,
                todayContentColor = Primary,
                todayDateBorderColor = Primary
            )
        )
    }
}
