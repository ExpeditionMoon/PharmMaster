package com.moon.pharm.domain.repository

import com.moon.pharm.domain.model.medication.IntakeRecord
import com.moon.pharm.domain.model.medication.Medication
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow

interface MedicationRepository {
    fun getMedications(userId: String): Flow<DataResourceResult<List<Medication>>>

    fun saveMedication(medication: Medication): Flow<DataResourceResult<Unit>>
    fun deleteMedication(medicationId: String): Flow<DataResourceResult<Unit>>

    fun getIntakeRecords(userId: String, date: String): Flow<DataResourceResult<List<IntakeRecord>>>
    fun saveIntakeRecord(record: IntakeRecord): Flow<DataResourceResult<Unit>>
    fun deleteIntakeRecord(medicationId: String, scheduleId: String, date: String): Flow<DataResourceResult<Unit>>
}