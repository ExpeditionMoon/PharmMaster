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
import com.moon.pharm.component_ui.component.dialog.PharmConfirmDialog
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews
import com.moon.pharm.domain.model.medication.MealTiming
import com.moon.pharm.domain.model.medication.MedicationType
import com.moon.pharm.domain.model.medication.RepeatType
import com.moon.pharm.domain.model.medication.TodayMedicationUiModel
import com.moon.pharm.profile.R

@Composable
fun MedicationItemCard(
    item: TodayMedicationUiModel,
    onTakeClick: (TodayMedicationUiModel) -> Unit,
    onDeleteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

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
                        text = " · ${item.type.label}",
                        fontSize = 13.sp,
                        color = PharmTheme.colors.secondFont
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${item.mealTiming.label} · ${item.repeatType.label}",
                    fontSize = 13.sp,
                    color = PharmTheme.colors.secondFont
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                MedicationCheckButton(
                    isTaken = item.isTaken,
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
                            text = {
                                Text(
                                    stringResource(R.string.medication_delete),
                                    color = MaterialTheme.colorScheme.error
                                )
                            },
                            onClick = {
                                showMenu = false
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }
        }
    }
    if (showDeleteDialog) {
        PharmConfirmDialog(
            title = stringResource(R.string.medication_delete_dialog_title),
            content = stringResource(R.string.medication_delete_dialog_content),
            confirmText = stringResource(R.string.medication_delete_desc),
            confirmTextColor = MaterialTheme.colorScheme.error,
            onConfirm = {
                onDeleteClick(item.medicationId)
            },
            onDismiss = {
                showDeleteDialog = false
            }
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
                    name = "혈압약",
                    type = MedicationType.OTC,
                    repeatType = RepeatType.DAILY,
                    time = "오전 08:00",
                    dosage = "1알",
                    mealTiming = MealTiming.AFTER_MEAL,
                    isTaken = false
                ),
                onTakeClick = {},
                onDeleteClick = {}
            )
        }
    }
}