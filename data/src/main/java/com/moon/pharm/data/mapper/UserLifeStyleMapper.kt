package com.moon.pharm.data.mapper

import com.moon.pharm.data.datasource.remote.dto.UserLifeStyleDTO
import com.moon.pharm.domain.model.auth.UserLifeStyle

fun UserLifeStyleDTO.toDomain(): UserLifeStyle {
    return UserLifeStyle(
        userId = this.userId,
        breakfastTime = this.breakfastTime,
        lunchTime = this.lunchTime,
        dinnerTime = this.dinnerTime,
        isNotificationEnabled = this.isNotificationEnabled
    )
}

fun UserLifeStyle.toDto(): UserLifeStyleDTO {
    return UserLifeStyleDTO(
        userId = this.userId,
        breakfastTime = this.breakfastTime,
        lunchTime = this.lunchTime,
        dinnerTime = this.dinnerTime,
        isNotificationEnabled = this.isNotificationEnabled
    )
}