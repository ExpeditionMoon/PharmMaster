package com.moon.pharm.profile.auth.screen.section

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.component.button.PharmPrimaryButton
import com.moon.pharm.component_ui.theme.Secondary
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.profile.R
import com.moon.pharm.profile.auth.model.SignUpStep
import com.moon.pharm.profile.auth.screen.SignUpUiState

@Composable
fun SignUpButtonSection(
    uiState: SignUpUiState,
    onNextClick: () -> Unit
) {
    val buttonText = when (uiState.currentStep) {
        SignUpStep.TYPE -> stringResource(R.string.signup_button_next)
        SignUpStep.EMAIL -> stringResource(R.string.signup_button_next)
        SignUpStep.NICKNAME ->
            if (uiState.userType == UserType.PHARMACIST) stringResource(R.string.signup_button_next)
            else stringResource(R.string.signup_button_complete)
        SignUpStep.PHARMACIST_INFO -> stringResource(R.string.signup_button_complete)
    }

    val isNextEnabled = !uiState.isLoading && when (uiState.currentStep) {
        SignUpStep.TYPE -> true
        SignUpStep.EMAIL -> (uiState.isEmailAvailable == true) && (uiState.password.length >= 6)
        SignUpStep.NICKNAME -> uiState.nickName.isNotBlank()
        SignUpStep.PHARMACIST_INFO -> uiState.pharmacyName.isNotBlank() && uiState.pharmacistBio.isNotBlank()
    }

    PharmPrimaryButton(
        text = buttonText,
        onClick = onNextClick,
        enabled = isNextEnabled,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        containerColor = Secondary
    )
}