package com.moon.pharm.domain.model.consult

enum class ConsultStatus(val label: String) {
    WAITING("답변 대기"),
    COMPLETED("답변 완료");

    companion object {
        fun from(value: String?): ConsultStatus {
            return entries.find { it.name == value } ?: WAITING
        }
    }
}
