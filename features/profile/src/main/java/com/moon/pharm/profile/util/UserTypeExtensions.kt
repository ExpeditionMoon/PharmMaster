package com.moon.pharm.profile.util

import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.profile.R

val UserType.labelRes: Int
    get() = when (this) {
        UserType.GENERAL -> R.string.signup_type_general
        UserType.PHARMACIST -> R.string.signup_type_pharmacist
    }

val UserType.apiValue: String
    get() = when (this) {
        UserType.GENERAL -> "일반"
        UserType.PHARMACIST -> "약사"
    }