package com.moon.pharm.profile.auth.mapper

import android.content.Context
import com.moon.pharm.profile.R
import com.moon.pharm.profile.auth.model.LoginUiMessage

fun LoginUiMessage.asString(context: Context): String {
    return when (this) {
        LoginUiMessage.EmptyEmail -> context.getString(R.string.login_error_empty_email)
        LoginUiMessage.EmptyPassword -> context.getString(R.string.login_error_empty_password)
        LoginUiMessage.LoginFailed -> context.getString(R.string.login_failed_default)
    }
}
