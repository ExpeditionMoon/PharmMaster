package com.moon.pharm.component_ui.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.theme.Black
import com.moon.pharm.component_ui.theme.Placeholder
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.theme.tertiaryLight

@Composable
fun DateSettingCard(
    placeholder: String,
    value: String,
    modifier: Modifier = Modifier,
    showCalendarIcon: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(White, RoundedCornerShape(10.dp))
            .border(0.5.dp, tertiaryLight, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = value.ifEmpty { placeholder },
            fontSize = 15.sp,
            color = if (value.isEmpty()) Placeholder else Black
        )

        if (showCalendarIcon) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(24.dp)
            )
        }
    }
}

