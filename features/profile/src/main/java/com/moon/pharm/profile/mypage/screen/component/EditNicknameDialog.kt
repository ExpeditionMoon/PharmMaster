package com.moon.pharm.profile.mypage.screen.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews
import com.moon.pharm.profile.R
import com.moon.pharm.component_ui.R as UiR

@Composable
fun EditNicknameDialog(
    currentNickname: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf(currentNickname) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = PharmTheme.colors.background,
        titleContentColor = PharmTheme.colors.onSurface,
        textContentColor = PharmTheme.colors.onSurface,
        shape = RoundedCornerShape(10.dp),
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(stringResource(R.string.signup_nickname_label)) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PharmTheme.colors.primary,
                    focusedLabelColor = PharmTheme.colors.primary,
                    cursorColor = PharmTheme.colors.primary,
                    unfocusedContainerColor = PharmTheme.colors.surface,
                    focusedContainerColor = PharmTheme.colors.surface
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (text.isNotBlank() && text != currentNickname) {
                        onConfirm(text)
                    } else {
                        onDismiss()
                    }
                }
            ) {
                Text(
                    text = stringResource(UiR.string.common_confirm),
                    color = PharmTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(UiR.string.common_cancel),
                    color = PharmTheme.colors.primary
                )
            }
        }
    )
}

@ThemePreviews
@Composable
private fun EditNicknameDialogPreview() {
    PharmMasterTheme {
        EditNicknameDialog(
            currentNickname = "기존닉네임",
            onDismiss = {},
            onConfirm = {}
        )
    }
}