package com.moon.pharm.consult.mapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.moon.pharm.consult.R
import com.moon.pharm.consult.model.ConsultUiMessage

@Composable
fun ConsultUiMessage.asString(): String {
    return when (this) {
        ConsultUiMessage.InputRequired -> stringResource(R.string.consult_error_input_required)
        ConsultUiMessage.TitleTooShort -> stringResource(R.string.consult_error_title_short)
        ConsultUiMessage.PharmacistRequired -> stringResource(R.string.consult_error_pharmacist_required)
        ConsultUiMessage.CreateFailed -> stringResource(R.string.consult_create_failed)
        ConsultUiMessage.AnswerRegisterSuccess -> stringResource(R.string.consult_answer_success)
    }
}