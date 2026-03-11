package com.moon.pharm.consult.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.component.button.PharmSmallButton
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews
import com.moon.pharm.consult.R

@Composable
fun InfoCard(label: String, content: String, onEditClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(PharmTheme.colors.surface, RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = PharmTheme.colors.secondFont,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = content,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PharmTheme.colors.primary,
                    maxLines = 2
                )
            }

            PharmSmallButton(
                text = stringResource(R.string.consult_confirm_edit_btn),
                onClick = onEditClick,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@ThemePreviews
@Composable
private fun InfoCardPreview() {
    PharmMasterTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            InfoCard(
                label = "지정 약국",
                content = "달빛약국 (김약사)",
                onEditClick = {}
            )
        }
    }
}