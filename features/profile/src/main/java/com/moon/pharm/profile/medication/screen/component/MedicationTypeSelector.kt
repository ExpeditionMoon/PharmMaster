package com.moon.pharm.profile.medication.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.Tertiary
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.domain.model.MedicationType

@Composable
fun MedicationTypeSelector(
    selectedType: MedicationType,
    onTypeSelected: (MedicationType) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MedicationType.entries.forEach { type ->
            val isSelected = selectedType == type
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(32.dp)
                    .background(
                        color = if (isSelected) Primary else Tertiary,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .clickable { onTypeSelected(type) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = type.label,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) White else SecondFont
                )
            }
        }
    }
}