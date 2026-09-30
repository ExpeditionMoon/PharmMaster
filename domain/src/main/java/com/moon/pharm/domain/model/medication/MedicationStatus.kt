package com.moon.pharm.domain.model.medication

enum class MedicationStatus {
    ACTIVE,
    PAUSED,
    ENDED;

    companion object {
        fun from(value: String?): MedicationStatus =
            entries.find { it.name == value } ?: ACTIVE
    }
}
