package com.moon.pharm.consult.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.moon.pharm.consult.R

@Composable
fun ConsultUiMessage.asString(): String {
    return when (this) {
        ConsultUiMessage.InputRequired -> stringResource(R.string.consult_error_input_required)
        ConsultUiMessage.TitleTooShort -> stringResource(R.string.consult_error_title_short)
        ConsultUiMessage.PharmacistRequired -> stringResource(R.string.consult_error_pharmacist_required)
        ConsultUiMessage.LoginRequired -> stringResource(R.string.consult_error_login_required)
        ConsultUiMessage.CreateFailed -> stringResource(R.string.consult_create_failed)
    }
}