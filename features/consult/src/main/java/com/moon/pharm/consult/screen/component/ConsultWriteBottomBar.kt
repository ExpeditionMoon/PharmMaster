package com.moon.pharm.consult.screen.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.component.button.PharmPrimaryButton
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.util.ThemePreviews
import com.moon.pharm.consult.R

@Composable
fun ConsultWriteBottomBar(
    isPublic: Boolean,
    isButtonEnabled: Boolean,
    isEditMode: Boolean,
    onVisibilityChange: (Boolean) -> Unit,
    onNextClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        ConsultPrivacyToggle(
            isPublic = isPublic,
            onVisibilityChange = onVisibilityChange
        )

        Spacer(modifier = Modifier.height(10.dp))

        PharmPrimaryButton(
            text = if (isEditMode) stringResource(R.string.consult_button_edit) else stringResource(R.string.consult_button_next),
            onClick = onNextClick,
            enabled = isButtonEnabled
        )
    }
}

@ThemePreviews
@Composable
private fun ConsultWriteBottomBarPreview() {
    PharmMasterTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            ConsultWriteBottomBar(
                isPublic = true,
                isButtonEnabled = true,
                isEditMode = true,
                onVisibilityChange = {},
                onNextClick = {}
            )
        }
    }
}