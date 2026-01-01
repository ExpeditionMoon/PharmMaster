package com.moon.pharm.component_ui.component.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.R
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.Tertiary
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.theme.onPrimaryContainerLight
import com.moon.pharm.component_ui.theme.primaryContainerLight
import com.moon.pharm.component_ui.theme.tertiaryLight
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false,
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onConfirm(timePickerState) }) {
                Text(
                    stringResource(R.string.common_confirm),
                    color = Primary, fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    stringResource(R.string.common_cancel),
                    color = Primary
                )
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.medication_alarm_time_dialog_title),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                MaterialTheme(
                    colorScheme = MaterialTheme.colorScheme.copy(
                        primary = Primary
                    )
                ) {
                    TimeInput(
                        state = timePickerState,
                        colors = TimePickerDefaults.colors(
                            containerColor = White,
                            timeSelectorSelectedContainerColor = primaryContainerLight,
                            timeSelectorSelectedContentColor = onPrimaryContainerLight,

                            timeSelectorUnselectedContainerColor = White,
                            timeSelectorUnselectedContentColor = Tertiary,

                            periodSelectorSelectedContainerColor = primaryContainerLight,
                            periodSelectorSelectedContentColor = onPrimaryContainerLight,
                            periodSelectorUnselectedContainerColor = White,
                            periodSelectorUnselectedContentColor = Tertiary,
                            periodSelectorBorderColor = tertiaryLight
                        )
                    )
                }
            }
        },
        shape = RoundedCornerShape(10.dp),
        containerColor = White
    )
}