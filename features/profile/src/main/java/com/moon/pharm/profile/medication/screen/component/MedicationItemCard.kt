package com.moon.pharm.profile.medication.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.designsystem.component.dialog.PharmConfirmDialog
import com.moon.pharm.designsystem.theme.PharmMasterTheme
import com.moon.pharm.designsystem.theme.PharmTheme
import com.moon.pharm.designsystem.util.ThemePreviews
import com.moon.pharm.profile.R
import com.moon.pharm.profile.medication.model.MealTimingUiModel
import com.moon.pharm.profile.medication.model.MedicationTypeUiModel
import com.moon.pharm.profile.medication.model.RepeatTypeUiModel
import com.moon.pharm.profile.medication.model.TodayMedicationUiModel

@Composable
fun MedicationItemCard(
    item: TodayMedicationUiModel,
    onTakeClick: (TodayMedicationUiModel) -> Unit,
    onEditClick: (String) -> Unit,
    onPauseClick: (String) -> Unit,
    onResumeClick: (String) -> Unit,
    onEndClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }
    var showEndDialog by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = PharmTheme.colors.surface,
            contentColor = PharmTheme.colors.primary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = PharmTheme.colors.onSurface
                    )
                    Text(
                        text = " · ${stringResource(item.type.labelRes)}",
                        fontSize = 13.sp,
                        color = PharmTheme.colors.secondFont
                    )
                    if (item.isPaused) {
                        Text(
                            text = " · ${stringResource(R.string.medication_paused_badge)}",
                            fontSize = 13.sp,
                            color = PharmTheme.colors.warning
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${stringResource(item.mealTiming.labelRes)} · ${stringResource(item.repeatType.labelRes)}",
                    fontSize = 13.sp,
                    color = PharmTheme.colors.secondFont
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                MedicationCheckButton(
                    isTaken = item.isTaken,
                    enabled = !item.isPaused,
                    onClick = { onTakeClick(item) }
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
                            text = { Text(stringResource(R.string.medication_edit)) },
                            onClick = {
                                showMenu = false
                                onEditClick(item.medicationId)
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    stringResource(
                                        if (item.isPaused) R.string.medication_resume else R.string.medication_pause
                                    )
                                )
                            },
                            onClick = {
                                showMenu = false
                                if (item.isPaused) onResumeClick(item.medicationId)
                                else onPauseClick(item.medicationId)
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(R.string.medication_end),
                                    color = MaterialTheme.colorScheme.error
                                )
                            },
                            onClick = {
                                showMenu = false
                                showEndDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    if (showEndDialog) {
        PharmConfirmDialog(
            title = stringResource(R.string.medication_end_dialog_title),
            content = stringResource(R.string.medication_end_dialog_content),
            confirmText = stringResource(R.string.medication_end_desc),
            confirmTextColor = MaterialTheme.colorScheme.error,
            onConfirm = { onEndClick(item.medicationId) },
            onDismiss = { showEndDialog = false }
        )
    }
}

@ThemePreviews
@Composable
private fun MedicationItemCardPreview() {
    PharmMasterTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            MedicationItemCard(
                item = TodayMedicationUiModel(
                    medicationId = "m1",
                    scheduleId = "s1",
                    name = "Tylenol",
                    type = MedicationTypeUiModel.Otc,
                    repeatType = RepeatTypeUiModel.Daily,
                    time = "08:00",
                    dosage = "1 tablet",
                    mealTiming = MealTimingUiModel.AfterMeal,
                    isPaused = false,
                    isTaken = false
                ),
                onTakeClick = {},
                onEditClick = {},
                onPauseClick = {},
                onResumeClick = {},
                onEndClick = {}
            )
        }
    }
}
