package com.moon.pharm.profile.auth.mapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.moon.pharm.profile.R
import com.moon.pharm.profile.auth.model.LoginUiMessage

@Composable
fun LoginUiMessage.asString(): String {
    return when (this) {
        LoginUiMessage.EmptyEmail -> stringResource(R.string.login_error_empty_email)
        LoginUiMessage.EmptyPassword -> stringResource(R.string.login_error_empty_password)
        LoginUiMessage.LoginFailed -> stringResource(R.string.login_failed_default)
    }
}