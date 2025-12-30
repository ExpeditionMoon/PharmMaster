package com.moon.pharm.domain.repository

import com.moon.pharm.domain.model.MedicationItem
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow

interface MedicationRepository {
    fun createMedication(medicationItem: MedicationItem): Flow<DataResourceResult<Unit>>
    fun getMedicationItems(): Flow<DataResourceResult<List<MedicationItem>>>
}