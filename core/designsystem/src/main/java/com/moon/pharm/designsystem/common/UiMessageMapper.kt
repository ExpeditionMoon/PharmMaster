package com.moon.pharm.designsystem.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.moon.pharm.designsystem.R

@Composable
fun UiMessage.asString(): String =
    when (this) {
        UiMessage.LoadDataFailed -> stringResource(R.string.error_load_data)
        UiMessage.LoginRequired -> stringResource(R.string.error_login_required)
        is UiMessage.Error -> message
        else -> stringResource(R.string.error_unknown)
    }
