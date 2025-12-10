package com.moon.pharm.consult.model

import androidx.compose.ui.graphics.Color
import com.moon.pharm.component_ui.theme.onPrimaryLight
import com.moon.pharm.component_ui.theme.onSecondaryLight
import com.moon.pharm.component_ui.theme.primaryLight
import com.moon.pharm.component_ui.theme.secondaryLight

data class ConsultItem(
    val id: String,
    val title: String,
    val timeAgo: String,
    val status: ConsultStatus,
    val isNew: Boolean,
    val author: String = "",
    val content: String = ""
) {
    companion object {}
}

enum class ConsultStatus(val label: String, val color: Color, val textColor: Color){
    WAITING("답변 대기", secondaryLight, onSecondaryLight),
    COMPLETED("답변 완료", primaryLight, onPrimaryLight)
}
