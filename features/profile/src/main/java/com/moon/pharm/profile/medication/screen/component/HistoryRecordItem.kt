package com.moon.pharm.profile.medication.screen.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moon.pharm.designsystem.component.item.PharmListItem
import com.moon.pharm.designsystem.theme.PharmMasterTheme
import com.moon.pharm.designsystem.theme.PharmTheme
import com.moon.pharm.designsystem.util.ThemePreviews
import com.moon.pharm.profile.R
import com.moon.pharm.profile.medication.model.HistoryRecordUiModel

@Composable
fun HistoryRecordItem(
    uiModel: HistoryRecordUiModel,
    onRecordClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (uiModel.isTaken) PharmTheme.colors.success else PharmTheme.colors.warning
    val backgroundColor = if (uiModel.isTaken) {
        PharmTheme.colors.successContainer
    } else {
        PharmTheme.colors.warningContainer
    }

    PharmListItem(
        modifier = modifier,
        onClick = onRecordClick,
        containerColor = backgroundColor,
        borderColor = borderColor,
        contentPadding = 10.dp,
        headline = uiModel.medicationName,
        subhead = if (uiModel.isTaken) {
            androidx.compose.ui.res.stringResource(R.string.medication_take_on)
        } else {
            androidx.compose.ui.res.stringResource(R.string.medication_take_off)
        },
        trailing = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (uiModel.isTaken) Icons.Default.CheckCircle else Icons.Default.Close,
                    contentDescription = null,
                    tint = borderColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    )
}

@ThemePreviews
@Composable
private fun HistoryRecordItemPreview() {
    PharmMasterTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            HistoryRecordItem(
                uiModel = HistoryRecordUiModel(
                    recordId = "record_1",
                    medicationId = "m1",
                    scheduleId = "s1",
                    recordDate = "2026-03-08",
                    isTaken = true,
                    medicationName = "Cold medicine",
                    time = "08:00"
                ),
                onRecordClick = {}
            )
        }
    }
}
