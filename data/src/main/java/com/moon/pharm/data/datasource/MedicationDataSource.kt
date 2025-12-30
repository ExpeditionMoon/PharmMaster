package com.moon.pharm.data.datasource

import com.moon.pharm.domain.model.MedicationItem
import kotlinx.coroutines.flow.Flow

interface MedicationDataSource {
    suspend fun create(medication: MedicationItem)

    fun getMedicationItems(): Flow<List<MedicationItem>>
}