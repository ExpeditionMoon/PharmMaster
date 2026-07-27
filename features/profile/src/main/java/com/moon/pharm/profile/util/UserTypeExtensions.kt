package com.moon.pharm.profile.util

import com.moon.pharm.profile.auth.model.UserTypeUiModel
import com.moon.pharm.profile.R

val UserTypeUiModel.labelRes: Int
    get() = when (this) {
        UserTypeUiModel.General -> R.string.signup_type_general
        UserTypeUiModel.Pharmacist -> R.string.signup_type_pharmacist
    }
