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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews
import com.moon.pharm.domain.model.medication.MealTiming
import com.moon.pharm.domain.model.medication.MedicationType
import com.moon.pharm.domain.model.medication.RepeatType
import com.moon.pharm.domain.model.medication.TodayMedicationUiModel

@Composable
fun MedicationItemCard(
    item: TodayMedicationUiModel,
    onTakeClick: (TodayMedicationUiModel) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = PharmTheme.colors.surface,
            contentColor = PharmTheme.colors.primary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
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
            MedicationCheckButton(
                isTaken = item.isTaken,
                onClick = { onTakeClick(item) }
            )
        }
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
                onTakeClick = {}
            )
        }
    }
}