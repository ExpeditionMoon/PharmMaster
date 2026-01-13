package com.moon.pharm.domain.model.auth

data class UserLifeStyle(
    val userId: String,
    val breakfastTime: Long = DEFAULT_BREAKFAST,
    val lunchTime: Long = DEFAULT_LUNCH,
    val dinnerTime: Long = DEFAULT_DINNER,
    val isNotificationEnabled: Boolean = true
) {
    companion object {
        const val DEFAULT_BREAKFAST = 28_800_000L
        const val DEFAULT_LUNCH = 45_000_000L
        const val DEFAULT_DINNER = 66_600_000L
    }
}