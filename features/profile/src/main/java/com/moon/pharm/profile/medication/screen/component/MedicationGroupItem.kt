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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.designsystem.theme.PharmMasterTheme
import com.moon.pharm.designsystem.theme.PharmTheme
import com.moon.pharm.designsystem.util.ThemePreviews
import com.moon.pharm.designsystem.util.toDisplayTimeString
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
    onEndClick: (String) -> Unit
) {
    val displayTime = group.time?.toLongOrNull()?.toDisplayTimeString() ?: group.time

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

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            group.items.forEach { item ->
                MedicationItemCard(
                    item = item,
                    onTakeClick = onTakeClick,
                    onEditClick = onEditClick,
                    onPauseClick = onPauseClick,
                    onResumeClick = onResumeClick,
                    onEndClick = onEndClick
                )
            }
        }
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
                onEndClick = {}
            )
        }
    }
}
