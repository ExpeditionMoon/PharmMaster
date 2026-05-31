package com.moon.pharm.consult.mapper

import android.content.Context
import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.component_ui.component.snackbar.SnackbarType
import com.moon.pharm.consult.R
import com.moon.pharm.consult.model.ConsultUiMessage
import com.moon.pharm.component_ui.R as ComponentUiR

fun UiMessage.asConsultString(context: Context): String {
    return when (this) {
        UiMessage.LoadDataFailed -> context.getString(ComponentUiR.string.error_load_data)
        UiMessage.LoginRequired -> context.getString(ComponentUiR.string.error_login_required)
        is UiMessage.Error -> message
        is ConsultUiMessage -> asConsultString(context)
        else -> context.getString(ComponentUiR.string.error_unknown)
    }
}

fun UiMessage.toConsultSnackbarType(): SnackbarType {
    return when (this) {
        ConsultUiMessage.ConsultUpdateSuccess,
        ConsultUiMessage.ConsultDeleteSuccess,
        ConsultUiMessage.AnswerRegisterSuccess,
        ConsultUiMessage.AnswerDeleteSuccess -> SnackbarType.INFO
        else -> SnackbarType.ERROR
    }
}

private fun ConsultUiMessage.asConsultString(context: Context): String {
    return when (this) {
        ConsultUiMessage.InputRequired -> context.getString(R.string.consult_error_input_required)
        ConsultUiMessage.TitleTooShort -> context.getString(R.string.consult_error_title_short)
        ConsultUiMessage.PharmacistRequired -> context.getString(R.string.consult_error_pharmacist_required)
        ConsultUiMessage.CreateFailed -> context.getString(R.string.consult_create_failed)
        ConsultUiMessage.ConsultUpdateSuccess -> context.getString(R.string.consult_update_success)
        ConsultUiMessage.ConsultDeleteSuccess -> context.getString(R.string.consult_delete_success)
        ConsultUiMessage.AnswerRegisterSuccess -> context.getString(R.string.consult_answer_success)
        ConsultUiMessage.AnswerDeleteSuccess -> context.getString(R.string.consult_delete_success)
    }
}
