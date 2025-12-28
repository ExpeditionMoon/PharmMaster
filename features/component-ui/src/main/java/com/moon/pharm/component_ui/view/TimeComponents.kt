package com.moon.pharm.component_ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.theme.Placeholder
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.Tertiary
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.theme.onPrimaryContainerLight
import com.moon.pharm.component_ui.theme.primaryContainerLight
import com.moon.pharm.component_ui.theme.tertiaryLight
import java.util.Calendar

@Composable
fun TimeSettingCard(
    time: String,
    onTimeClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White, RoundedCornerShape(10.dp))
            .border(0.5.dp, tertiaryLight, RoundedCornerShape(10.dp))
            .clickable { onTimeClick() }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = time,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Placeholder
        )

        Spacer(modifier = Modifier.width(10.dp))

        Icon(
            imageVector = Icons.Default.Alarm,
            contentDescription = null,
            tint = Primary,
            modifier = Modifier
                .size(24.dp)
        )
    }
}

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
        is24Hour = true,
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onConfirm(timePickerState) }) {
                Text("확인", color = Primary, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소", color = Primary)
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "알람 시간 설정",
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
                        )
                    )
                }
            }
        },
        shape = RoundedCornerShape(10.dp),
        containerColor = White
    )
}