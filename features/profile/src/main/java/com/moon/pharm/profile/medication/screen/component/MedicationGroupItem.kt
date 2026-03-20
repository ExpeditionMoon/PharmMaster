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
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews
import com.moon.pharm.component_ui.util.toDisplayTimeString
import com.moon.pharm.domain.model.medication.MealTiming
import com.moon.pharm.domain.model.medication.MedicationTimeGroup
import com.moon.pharm.domain.model.medication.MedicationType
import com.moon.pharm.domain.model.medication.RepeatType
import com.moon.pharm.domain.model.medication.TodayMedicationUiModel

@Composable
fun MedicationGroupItem(
    group: MedicationTimeGroup,
    onTakeClick: (TodayMedicationUiModel) -> Unit,
    onDeleteClick: (String) -> Unit
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
                text = displayTime ?: "",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = PharmTheme.colors.secondFont
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            group.items.forEach { item ->
                MedicationItemCard(item = item, onTakeClick = onTakeClick, onDeleteClick = onDeleteClick)
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
                group = MedicationTimeGroup(
                    time = "08:00",
                    items = listOf(
                        TodayMedicationUiModel(
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
                    )
                ),
                onTakeClick = {},
                onDeleteClick = {}
            )
        }
    }
}