package com.moon.pharm.component_ui.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews

enum class InfoCardType {
    NOTICE,
    SAFE,
    RISK
}

@Composable
fun PharmInfoCard(
    modifier: Modifier = Modifier,
    message: String,
    title: String? = null,
    type: InfoCardType = InfoCardType.NOTICE,
    customIcon: ImageVector? = null
) {
    val (containerColor, contentColor, defaultIcon) = when (type) {
        InfoCardType.NOTICE -> Triple(
            PharmTheme.colors.infoContainer.copy(alpha = 0.3f),
            PharmTheme.colors.onInfoContainer,
            Icons.Outlined.Lock 
        )
        InfoCardType.SAFE -> Triple(
            PharmTheme.colors.successContainer,
            PharmTheme.colors.success,
            Icons.Outlined.CheckCircle
        )
        InfoCardType.RISK -> Triple(
            PharmTheme.colors.warningContainer,
            PharmTheme.colors.warning,
            Icons.Outlined.Warning
        )
    }

    val icon = customIcon ?: defaultIcon

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = containerColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            if (title != null) {
                Text(
                    text = title,
                    color = contentColor,
                    style = PharmTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
            }

            Text(
                text = message,
                color = contentColor,
                style = PharmTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

@ThemePreviews
@Composable
private fun PharmInfoCardPreview() {
    PharmMasterTheme {
        Column(
            modifier = Modifier
                .background(PharmTheme.colors.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PharmInfoCard(
                message = "선택하신 이미지는 서버에 저장되지 않고\n기기에서만 안전하게 분석됩니다.",
                type = InfoCardType.NOTICE
            )

            PharmInfoCard(
                title = "안전한 조합",
                message = "스캔하신 약물들은 함께 복용해도 안전합니다.",
                type = InfoCardType.SAFE
            )

            PharmInfoCard(
                title = "약물 상호작용 주의",
                message = "타이레놀과 부루펜을 함께 복용하면 위장장애가 발생할 수 있습니다.",
                type = InfoCardType.RISK
            )
        }
    }
}