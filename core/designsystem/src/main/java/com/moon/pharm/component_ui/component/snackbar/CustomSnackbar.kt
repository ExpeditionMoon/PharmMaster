package com.moon.pharm.component_ui.component.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews

enum class SnackbarType {
    SUCCESS, ERROR, INFO
}

private data class SnackbarStyle(
    val containerColor: Color,
    val icon: ImageVector,
    val iconColor: Color,
    val textColor: Color
)

@Composable
fun CustomSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier,
    type: SnackbarType = SnackbarType.ERROR
) {
    val colors = PharmTheme.colors

    val style = remember(type) {
        when (type) {
            SnackbarType.SUCCESS -> SnackbarStyle(
                containerColor = colors.successContainer,
                icon = Icons.Default.CheckCircle,
                iconColor = colors.success,
                textColor = colors.onSurface.copy(alpha = 0.8f)
            )
            SnackbarType.ERROR -> SnackbarStyle(
                containerColor = colors.errorContainer,
                icon = Icons.Default.Error,
                iconColor = colors.error,
                textColor = colors.onErrorContainer
            )
            SnackbarType.INFO -> SnackbarStyle(
                containerColor = colors.infoContainer,
                icon = Icons.Default.Info,
                iconColor = colors.primary,
                textColor = colors.onInfoContainer
            )
        }
    }

    Box(
        modifier = modifier
            .padding(16.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(style.containerColor)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = style.icon,
                contentDescription = null,
                tint = style.iconColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = snackbarData.visuals.message,
                color = style.textColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// 프리뷰용 더미 데이터 클래스
private class PreviewSnackbarData(
    override val visuals: SnackbarVisuals
) : androidx.compose.material3.SnackbarData {
    override fun dismiss() {}
    override fun performAction() {}
}

private class PreviewSnackbarVisuals(
    override val message: String,
    override val actionLabel: String? = null,
    override val duration: androidx.compose.material3.SnackbarDuration = androidx.compose.material3.SnackbarDuration.Short,
    override val withDismissAction: Boolean = false
) : SnackbarVisuals

@ThemePreviews
@Composable
private fun CustomSnackbarPreview() {
    val dummyData = PreviewSnackbarData(
        visuals = PreviewSnackbarVisuals(message = "처방전 전송에 성공했습니다.")
    )

    PharmMasterTheme {
        Column(
            modifier = Modifier.background(PharmTheme.colors.background).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomSnackbar(snackbarData = dummyData, type = SnackbarType.SUCCESS)
            CustomSnackbar(snackbarData = PreviewSnackbarData(PreviewSnackbarVisuals("네트워크 오류가 발생했습니다.")), type = SnackbarType.ERROR)
            CustomSnackbar(snackbarData = PreviewSnackbarData(PreviewSnackbarVisuals("새로운 알림이 있습니다.")), type = SnackbarType.INFO)
        }
    }
}