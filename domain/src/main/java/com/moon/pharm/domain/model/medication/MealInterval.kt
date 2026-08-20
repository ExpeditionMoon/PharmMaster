package com.moon.pharm.domain.model.medication

enum class MealInterval(val minutes: Int) {
    IMMEDIATELY(0),
    THIRTY_MINUTES(30),
    ONE_HOUR(60)
}
