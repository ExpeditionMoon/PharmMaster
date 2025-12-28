package com.moon.pharm.data.datasource

import com.moon.pharm.domain.model.MedicationItem

interface MedicationDataSource {
    suspend fun create(medication: MedicationItem)
}