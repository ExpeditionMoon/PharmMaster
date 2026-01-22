package com.moon.pharm.profile.auth.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.moon.pharm.profile.R

@Composable
fun LoginUiMessage.asString(): String {
    return when (this) {
        LoginUiMessage.EmptyEmail -> stringResource(R.string.login_error_empty_email)
        LoginUiMessage.EmptyPassword -> stringResource(R.string.login_error_empty_password)
        LoginUiMessage.LoginFailed -> stringResource(R.string.login_failed_default)
    }
}