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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.designsystem.theme.PharmMasterTheme
import com.moon.pharm.designsystem.theme.PharmTheme
import com.moon.pharm.designsystem.util.ThemePreviews
import com.moon.pharm.profile.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationProgressCard(
    total: Int,
    completed: Int,
    weeklyTotal: Int,
    weeklyCompleted: Int
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = PharmTheme.colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.medication_progress_today, completed, total),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = PharmTheme.colors.onSurface
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { if (total > 0) completed.toFloat() / total.toFloat() else 0f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = PharmTheme.colors.primary,
                trackColor = PharmTheme.colors.tertiary,
                gapSize = 0.dp,
            )

            if (weeklyTotal > 0) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(
                        R.string.medication_progress_weekly,
                        weeklyCompleted * 100 / weeklyTotal
                    ),
                    fontSize = 13.sp,
                    color = PharmTheme.colors.secondFont
                )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun MedicationProgressCardPreview() {
    PharmMasterTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            MedicationProgressCard(
                total = 5,
                completed = 3,
                weeklyTotal = 8,
                weeklyCompleted = 6
            )
        }
    }
}
