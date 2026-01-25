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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.util.toDisplayTimeString
import com.moon.pharm.domain.model.medication.MedicationTimeGroup
import com.moon.pharm.domain.model.medication.TodayMedicationUiModel

@Composable
fun MedicationGroupItem(
    group: MedicationTimeGroup,
    onTakeClick: (TodayMedicationUiModel) -> Unit
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
                    .background(Color(0xFFFFC107))
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = displayTime ?: "",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = SecondFont
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            group.items.forEach { item ->
                MedicationItemCard(item = item, onTakeClick = onTakeClick)
            }
        }
    }
}