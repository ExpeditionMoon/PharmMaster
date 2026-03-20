package com.moon.pharm.profile.medication.screen.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.component.dialog.PharmConfirmDialog
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
    onDeleteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val record = uiModel.record
    val borderColor = if (record.isTaken) PharmTheme.colors.success else PharmTheme.colors.warning
    val backgroundColor = if (record.isTaken) PharmTheme.colors.successContainer else PharmTheme.colors.warningContainer

    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (record.isTaken) Icons.Default.CheckCircle else Icons.Default.Close,
                    contentDescription = null,
                    tint = borderColor,
                    modifier = Modifier.size(24.dp)
                )

                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(R.string.medication_option_menu_desc),
                            tint = PharmTheme.colors.secondFont
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.medication_delete), color = MaterialTheme.colorScheme.error) },
                            onClick = {
                                showMenu = false
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }
        }
    )
    if (showDeleteDialog) {
        PharmConfirmDialog(
            title = stringResource(R.string.medication_delete_dialog_title),
            content = stringResource(R.string.medication_delete_dialog_content),
            confirmText = stringResource(R.string.medication_delete_desc),
            confirmTextColor = MaterialTheme.colorScheme.error,
            onConfirm = { onDeleteClick(uiModel.record.medicationId) },
            onDismiss = { showDeleteDialog = false }
        )
    }
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
                onRecordClick = {},
                onDeleteClick = {}
            )
        }
    }
}