package com.moon.pharm.profile.mypage.mapper

import android.content.Context
import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.component_ui.R as ComponentUiR

fun UiMessage.asMyPageString(context: Context): String {
    return when (this) {
        UiMessage.LoadDataFailed -> context.getString(ComponentUiR.string.error_load_data)
        UiMessage.LoginRequired -> context.getString(ComponentUiR.string.error_login_required)
        is UiMessage.Error -> message
        else -> context.getString(ComponentUiR.string.error_unknown)
    }
}
