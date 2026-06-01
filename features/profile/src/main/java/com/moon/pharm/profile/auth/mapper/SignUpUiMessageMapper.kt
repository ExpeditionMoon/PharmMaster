package com.moon.pharm.profile.auth.mapper

import android.content.Context
import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.profile.R
import com.moon.pharm.profile.auth.model.SignUpUiMessage
import com.moon.pharm.component_ui.R as ComponentUiR

fun UiMessage.asSignUpString(context: Context): String {
    return when (this) {
        is SignUpUiMessage -> asString(context)
        UiMessage.LoadDataFailed -> context.getString(ComponentUiR.string.error_load_data)
        UiMessage.LoginRequired -> context.getString(ComponentUiR.string.error_login_required)
        is UiMessage.Error -> message
        else -> context.getString(ComponentUiR.string.error_unknown)
    }
}

private fun SignUpUiMessage.asString(context: Context): String {
    return when (this) {
        SignUpUiMessage.SignUpFailed -> context.getString(R.string.signup_error_failed)

        SignUpUiMessage.EmptyEmail -> context.getString(R.string.login_error_empty_email)
        SignUpUiMessage.InvalidEmailFormat -> context.getString(R.string.signup_error_email_format)
        SignUpUiMessage.EmailDuplicated -> context.getString(R.string.signup_email_duplicated)
        SignUpUiMessage.EmailAvailable -> context.getString(R.string.signup_email_available)
        SignUpUiMessage.PasswordTooShort -> context.getString(R.string.signup_error_password_short)

        SignUpUiMessage.EmptyNickname -> context.getString(R.string.signup_nickname_placeholder)

        SignUpUiMessage.EmptyPharmacy -> context.getString(R.string.signup_error_pharmacy_required)
        SignUpUiMessage.EmptyBio -> context.getString(R.string.signup_error_bio_required)
    }
}
