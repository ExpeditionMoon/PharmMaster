package com.moon.pharm.data.datasource.remote.dto

import com.moon.pharm.data.common.EMPTY_STRING
import com.moon.pharm.domain.model.auth.UserLifeStyle

data class UserLifeStyleDTO(
    val userId: String = EMPTY_STRING,
    val breakfastTime: Long = UserLifeStyle.DEFAULT_BREAKFAST,
    val lunchTime: Long = UserLifeStyle.DEFAULT_LUNCH,
    val dinnerTime: Long = UserLifeStyle.DEFAULT_DINNER,
    val isNotificationEnabled: Boolean = true
)

// UserLifeStyle 매퍼
fun UserLifeStyleDTO.toDomain(): UserLifeStyle = UserLifeStyle(
    userId = this.userId,
    breakfastTime = this.breakfastTime,
    lunchTime = this.lunchTime,
    dinnerTime = this.dinnerTime,
    isNotificationEnabled = this.isNotificationEnabled
)

fun UserLifeStyle.toDto(): UserLifeStyleDTO = UserLifeStyleDTO(
    userId = this.userId,
    breakfastTime = this.breakfastTime,
    lunchTime = this.lunchTime,
    dinnerTime = this.dinnerTime,
    isNotificationEnabled = this.isNotificationEnabled
)