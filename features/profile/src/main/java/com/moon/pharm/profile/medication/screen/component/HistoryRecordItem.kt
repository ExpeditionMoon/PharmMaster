package com.moon.pharm.profile.medication.screen.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.component.item.PharmListItem
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews
import com.moon.pharm.domain.model.medication.IntakeRecord
import com.moon.pharm.profile.R
import com.moon.pharm.profile.medication.model.HistoryRecordUiModel

@Composable
fun HistoryRecordItem(
    uiModel: HistoryRecordUiModel,
    onRecordClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val record = uiModel.record
    val borderColor = if (record.isTaken) PharmTheme.colors.success else PharmTheme.colors.warning
    val backgroundColor = if (record.isTaken) PharmTheme.colors.successContainer else PharmTheme.colors.warningContainer

    PharmListItem(
        modifier = modifier,
        onClick = onRecordClick,
        containerColor = backgroundColor,
        borderColor = borderColor,
        contentPadding = 10.dp,
        headline = uiModel.medicationName,
        subhead = if (record.isTaken) stringResource(R.string.medication_take_on)
        else stringResource(R.string.medication_take_off),
        trailing = {
            Icon(
                imageVector = if (record.isTaken) Icons.Default.CheckCircle else Icons.Default.Close,
                contentDescription = null,
                tint = borderColor,
                modifier = Modifier.size(24.dp)
            )
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
                    medicationName = "감기약 (아침)",
                    time = "오전 08:00",
                    record = IntakeRecord(
                        id = "record_1",
                        userId = "user_1",
                        medicationId = "m1",
                        scheduleId = "s1",
                        recordDate = "2026-03-08",
                        isTaken = true
                    )
                ),
                onRecordClick = {}
            )
        }
    }
}