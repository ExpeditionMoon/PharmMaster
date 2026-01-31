package com.moon.pharm.consult.util

import com.moon.pharm.consult.R
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.component_ui.R as UiR

val UserType.myConsultListTitleRes: Int
    get() = when (this) {
        UserType.PHARMACIST -> UiR.string.my_answer_title
        UserType.GENERAL -> UiR.string.my_consult_title
    }

val UserType.myConsultListEmptyTextRes: Int
    get() = when (this) {
        UserType.PHARMACIST -> R.string.my_answer_empty
        UserType.GENERAL -> R.string.my_consult_empty
    }