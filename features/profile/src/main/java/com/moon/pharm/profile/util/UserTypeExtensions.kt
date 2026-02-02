package com.moon.pharm.profile.util

import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.profile.R
import com.moon.pharm.component_ui.R as UiR

val UserType.labelRes: Int
    get() = when (this) {
        UserType.GENERAL -> R.string.signup_type_general
        UserType.PHARMACIST -> R.string.signup_type_pharmacist
    }

val UserType.myPageConsultMenuTitleRes: Int
    get() = when (this) {
        UserType.PHARMACIST -> UiR.string.my_answer_title
        UserType.GENERAL -> UiR.string.my_consult_title
    }