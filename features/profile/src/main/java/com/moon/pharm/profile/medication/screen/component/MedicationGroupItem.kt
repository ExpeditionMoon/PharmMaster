package com.moon.pharm.profile.medication.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.designsystem.component.dialog.PharmConfirmDialog
import com.moon.pharm.designsystem.theme.PharmMasterTheme
import com.moon.pharm.designsystem.theme.PharmTheme
import com.moon.pharm.designsystem.util.ThemePreviews
import com.moon.pharm.designsystem.util.toDisplayTimeString
import com.moon.pharm.domain.usecase.medication.MedicationGroupIntakeItem
import com.moon.pharm.profile.R
import com.moon.pharm.profile.medication.model.MealTimingUiModel
import com.moon.pharm.profile.medication.model.MedicationTimeGroupUiModel
import com.moon.pharm.profile.medication.model.MedicationTypeUiModel
import com.moon.pharm.profile.medication.model.RepeatTypeUiModel
import com.moon.pharm.profile.medication.model.TodayMedicationUiModel

@Composable
fun MedicationGroupItem(
    group: MedicationTimeGroupUiModel,
    onTakeClick: (TodayMedicationUiModel) -> Unit,
    onEditClick: (String) -> Unit,
    onPauseClick: (String) -> Unit,
    onResumeClick: (String) -> Unit,
    onEndClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
    onCompleteGroup: (List<MedicationGroupIntakeItem>) -> Unit
) {
    val displayTime = group.time?.toLongOrNull()?.toDisplayTimeString() ?: group.time
    var showGroupDetail by remember { mutableStateOf(false) }
    var showCompleteConfirmation by remember { mutableStateOf(false) }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(PharmTheme.colors.warning)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = displayTime.orEmpty(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = PharmTheme.colors.secondFont
            )
        }

        if (group.isIntakeGroup) {
            MedicationGroupSummaryCard(group = group, onClick = { showGroupDetail = true })
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                group.items.forEach { item ->
                    MedicationItemCard(
                        item = item,
                        onTakeClick = onTakeClick,
                        onEditClick = onEditClick,
                        onPauseClick = onPauseClick,
                        onResumeClick = onResumeClick,
                        onEndClick = onEndClick,
                        onDeleteClick = onDeleteClick
                    )
                }
            }
        }
    }

    if (showGroupDetail) {
        AlertDialog(
            onDismissRequest = { showGroupDetail = false },
            containerColor = PharmTheme.colors.tertiaryContainer,
            title = { Text(stringResource(R.string.medication_group_detail_title)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    group.items.forEach { item ->
                        MedicationItemCard(
                            item = item,
                            onTakeClick = onTakeClick,
                            onEditClick = onEditClick,
                            onPauseClick = onPauseClick,
                            onResumeClick = onResumeClick,
                            onEndClick = onEndClick,
                            onDeleteClick = onDeleteClick
                        )
                    }
                }
            },
            confirmButton = {
                if (group.completedCount < group.totalCount) {
                    TextButton(onClick = { showCompleteConfirmation = true }) {
                        Text(stringResource(R.string.medication_group_complete_all))
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showGroupDetail = false }) {
                    Text(stringResource(R.string.medication_group_close))
                }
            }
        )
    }

    if (showCompleteConfirmation) {
        PharmConfirmDialog(
            title = stringResource(R.string.medication_group_complete_all),
            content = stringResource(R.string.medication_group_complete_all_confirmation),
            confirmText = stringResource(R.string.medication_group_complete_all),
            onConfirm = {
                onCompleteGroup(group.items.map {
                    MedicationGroupIntakeItem(it.medicationId, it.scheduleId)
                })
                showCompleteConfirmation = false
                showGroupDetail = false
            },
            onDismiss = { showCompleteConfirmation = false }
        )
    }
}

@Composable
private fun MedicationGroupSummaryCard(
    group: MedicationTimeGroupUiModel,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = PharmTheme.colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = buildString {
                append(group.representativeName)
                if (group.totalCount > 1) append(" 외 ${group.totalCount - 1}개")
                append(" · ${group.completedCount}/${group.totalCount} 복용")
            },
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = PharmTheme.colors.onSurface,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@ThemePreviews
@Composable
private fun MedicationGroupItemPreview() {
    PharmMasterTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            MedicationGroupItem(
                group = MedicationTimeGroupUiModel(
                    time = "08:00",
                    items = listOf(
                        TodayMedicationUiModel(
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
                        )
                    )
                ),
                onTakeClick = {},
                onEditClick = {},
                onPauseClick = {},
                onResumeClick = {},
                onEndClick = {},
                onDeleteClick = {},
                onCompleteGroup = {}
            )
        }
    }
}
