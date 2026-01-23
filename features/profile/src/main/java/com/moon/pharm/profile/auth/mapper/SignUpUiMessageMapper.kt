package com.moon.pharm.profile.auth.mapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.moon.pharm.profile.R
import com.moon.pharm.profile.auth.common.SignUpUiMessage

@Composable
fun SignUpUiMessage.asString(): String {
    return when (this) {
        SignUpUiMessage.SignUpFailed -> stringResource(R.string.signup_error_failed)

        SignUpUiMessage.EmptyEmail -> stringResource(R.string.login_error_empty_email)
        SignUpUiMessage.InvalidEmailFormat -> stringResource(R.string.signup_error_email_format)
        SignUpUiMessage.EmailDuplicated -> stringResource(R.string.signup_email_duplicated)
        SignUpUiMessage.EmailAvailable -> stringResource(R.string.signup_email_available)
        SignUpUiMessage.PasswordTooShort -> stringResource(R.string.signup_error_password_short)

        SignUpUiMessage.EmptyNickname -> stringResource(R.string.signup_nickname_placeholder)

        SignUpUiMessage.EmptyPharmacy -> stringResource(R.string.signup_error_pharmacy_required)
        SignUpUiMessage.EmptyBio -> stringResource(R.string.signup_error_bio_required)
    }
}