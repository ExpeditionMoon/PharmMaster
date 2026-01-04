package com.moon.pharm.consult.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.moon.pharm.consult.R

@Composable
fun ConsultUiMessage.asString(): String {
    return when (this) {
        ConsultUiMessage.CreateFailed ->
            stringResource(R.string.error_create_consult)
    }
}