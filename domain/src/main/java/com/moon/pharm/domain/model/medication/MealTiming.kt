package com.moon.pharm.domain.model.medication

enum class MealTiming(val label: String) {
    BEFORE_MEAL("식전"),
    DURING_MEAL("식사 중"),
    AFTER_MEAL("식후"),
    NONE("상관없음")
}