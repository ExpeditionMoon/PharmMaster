package com.moon.pharm.data.mapper

import com.moon.pharm.data.datasource.remote.dto.UserLifeStyleDTO
import com.moon.pharm.domain.model.auth.UserLifeStyle

fun UserLifeStyleDTO.toDomain(): UserLifeStyle = UserLifeStyle(
    userId = userId,
    breakfastTime = breakfastTime,
    lunchTime = lunchTime,
    dinnerTime = dinnerTime,
    isNotificationEnabled = isNotificationEnabled
)

fun UserLifeStyle.toDto(): UserLifeStyleDTO = UserLifeStyleDTO(
    userId = userId,
    breakfastTime = breakfastTime,
    lunchTime = lunchTime,
    dinnerTime = dinnerTime,
    isNotificationEnabled = isNotificationEnabled
)
