package com.moon.pharm.consult.util

import com.moon.pharm.consult.R
import com.moon.pharm.designsystem.R as UiR

val Boolean.myConsultListTitleRes: Int
    get() = when (this) {
        true -> UiR.string.my_answer_title
        false -> UiR.string.my_consult_title
    }

val Boolean.myConsultListEmptyTextRes: Int
    get() = when (this) {
        true -> R.string.my_answer_empty
        false -> R.string.my_consult_empty
    }
