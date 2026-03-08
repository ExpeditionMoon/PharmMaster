package com.moon.pharm.component_ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    onMoreClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 0.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = PharmTheme.colors.onSurface
        )
        TextButton(
            onClick = onMoreClick,
            colors = ButtonDefaults.textButtonColors(
                contentColor = PharmTheme.colors.secondFont
            )
        ) {
            Text(
                text = "더보기 >",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@ThemePreviews
@Composable
private fun SectionHeaderPreview() {
    PharmMasterTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(PharmTheme.colors.background)
                .padding(16.dp)
        ) {
            SectionHeader(
                title = "나의 알림",
                onMoreClick = {}
            )
        }
    }
}
