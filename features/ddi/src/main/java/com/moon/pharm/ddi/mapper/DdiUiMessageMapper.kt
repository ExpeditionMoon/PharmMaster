package com.moon.pharm.ddi.mapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.moon.pharm.ddi.R
import com.moon.pharm.ddi.model.DdiUiMessage
import com.moon.pharm.domain.model.ddi.DdiException

fun DdiException.toUiMessage(): DdiUiMessage {
    return when (this) {
        is DdiException.AnalysisFailed -> DdiUiMessage.AnalysisFailed
        is DdiException.Network -> DdiUiMessage.NetworkError
        is DdiException.EmptyDrug -> DdiUiMessage.EmptyDrug
        is DdiException.Unknown -> {
            this.message?.let { DdiUiMessage.DynamicError(it) } ?: DdiUiMessage.Unknown
        }
    }
}
@Composable
fun DdiUiMessage.asString(): String {
    return when (this) {
        DdiUiMessage.AnalysisFailed -> stringResource(R.string.ddi_error_analysis_failed)
        DdiUiMessage.NetworkError -> stringResource(R.string.ddi_error_network)
        DdiUiMessage.EmptyDrug -> stringResource(R.string.ddi_error_empty_drug)
        DdiUiMessage.Unknown -> stringResource(R.string.ddi_error_unknown)
        is DdiUiMessage.DynamicError -> this.message
    }
}