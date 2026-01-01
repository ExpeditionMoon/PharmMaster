package com.moon.pharm.component_ui.component

import androidx.compose.foundation.layout.Arrangement
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
import com.moon.pharm.component_ui.theme.OnSurface
import com.moon.pharm.component_ui.theme.SecondFont

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
            color = OnSurface
        )
        TextButton(
            onClick = onMoreClick,
            colors = ButtonDefaults.textButtonColors(
                contentColor = SecondFont
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
