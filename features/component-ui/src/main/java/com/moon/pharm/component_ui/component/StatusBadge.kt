package com.moon.pharm.component_ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.theme.onPrimaryLight

@Composable
fun StatusBadge(
    text: String,
    statusColor: Color,
    modifier: Modifier = Modifier,
    contentColor: Color = onPrimaryLight,
    useBorderedStyle: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    val badgeShape = RoundedCornerShape(4.dp)

    val rowModifier = if (useBorderedStyle) {
        modifier
            .border(1.dp, statusColor, shape = badgeShape)
            .background(color = statusColor.copy(alpha = 0.1f), shape = badgeShape)
    } else {
        modifier
            .background(color = statusColor, shape = badgeShape)
    }.padding(horizontal = 8.dp, vertical = 4.dp)

    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            leadingIcon()
            Spacer(modifier = Modifier.width(4.dp))
        }

        val textColor = when {
            useBorderedStyle -> statusColor
            contentColor != onPrimaryLight -> contentColor
            else -> White
        }

        Text(
            text = text,
            color = textColor,
            fontSize = if (useBorderedStyle) 12.sp else 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
