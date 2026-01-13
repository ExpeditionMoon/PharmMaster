package com.moon.pharm.domain.model.medication

enum class MedicationType(val label: String) {
    PRESCRIPTION("처방약"),
    OTC("일반약"),
    SUPPLEMENT("비타민/영양제")
}