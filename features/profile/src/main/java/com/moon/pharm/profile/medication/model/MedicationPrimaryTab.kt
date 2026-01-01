package com.moon.pharm.profile.medication.model

import com.moon.pharm.profile.R

enum class MedicationPrimaryTab(val title: Int, val index: Int) {
    ALL(R.string.medication_category_all, 0),
    PRESCRIPTION(R.string.medication_category_prescription, 1),
    GENERAL(R.string.medication_category_general, 2),
    SUPPLEMENTS(R.string.medication_category_supplements, 3);

    companion object {
        fun fromIndex(index: Int) = entries.find { it.index == index } ?: ALL
    }
}