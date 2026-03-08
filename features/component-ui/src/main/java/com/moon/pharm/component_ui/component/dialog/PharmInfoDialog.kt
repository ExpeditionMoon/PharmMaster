package com.moon.pharm.component_ui.component.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews

@Composable
fun PharmInfoDialog(
    title: String,
    content: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        title = { Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
        text = { Text(text = content, fontSize = 12.sp, color = PharmTheme.colors.onSurface) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("닫기", color = PharmTheme.colors.onSurface)
            }
        },
        containerColor = PharmTheme.colors.surface,
        textContentColor = PharmTheme.colors.onSurface
    )
}

@ThemePreviews
@Composable
private fun PharmInfoDialogPreview() {
    PharmMasterTheme {
        PharmInfoDialog(
            title = "처방전 전송 완료",
            content = "약국으로 처방전이 성공적으로 전송되었습니다.\n조제 완료 알림을 기다려주세요.",
            onDismiss = {}
        )
    }
}