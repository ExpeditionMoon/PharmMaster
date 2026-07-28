package com.moon.pharm.component_ui.component.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.R
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews

/**
 * '확인' 및 '취소' 두 가지 액션이 필요한 경우 사용하는 공통 다이얼로그
 */
@Composable
fun PharmConfirmDialog(
    title: String,
    content: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    confirmText: String = stringResource(R.string.common_confirm),
    cancelText: String = stringResource(R.string.common_cancel),
    confirmTextColor: Color = PharmTheme.colors.primary
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        title = { Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
        text = { Text(text = content, fontSize = 14.sp, color = PharmTheme.colors.onSurface) },
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
                onDismiss()
            }) {
                Text(text = confirmText, color = confirmTextColor, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = cancelText, color = PharmTheme.colors.primary)
            }
        },
        containerColor = PharmTheme.colors.surface,
        textContentColor = PharmTheme.colors.onSurface
    )
}

@ThemePreviews
@Composable
private fun PharmConfirmDialogPreview() {
    PharmMasterTheme {
        PharmConfirmDialog(
            title = "상담글 삭제",
            content = "정말 삭제하시겠습니까?\n삭제된 글은 복구할 수 없습니다.",
            confirmText = "삭제",
            onConfirm = {},
            onDismiss = {}
        )
    }
}