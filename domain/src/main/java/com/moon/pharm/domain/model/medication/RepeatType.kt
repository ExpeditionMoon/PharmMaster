package com.moon.pharm.domain.model.medication

enum class RepeatType(val label: String) {
    DAILY("매일"),
    WEEKLY("매주"),
    PERIOD("특정 기간")
}