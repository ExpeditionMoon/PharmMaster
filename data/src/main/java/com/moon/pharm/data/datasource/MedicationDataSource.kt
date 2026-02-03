package com.moon.pharm.data.datasource

import com.moon.pharm.data.datasource.remote.dto.IntakeRecordDTO
import com.moon.pharm.data.datasource.remote.dto.MedicationDTO
import kotlinx.coroutines.flow.Flow

interface MedicationDataSource {
    fun getMedications(userId: String): Flow<List<MedicationDTO>>
    suspend fun saveMedication(medication: MedicationDTO)
    suspend fun deleteMedication(medicationId: String)

    fun getIntakeRecords(userId: String, date: String): Flow<List<IntakeRecordDTO>>
    fun getIntakeRecordsByRange(userId: String, startDate: String, endDate: String): Flow<List<IntakeRecordDTO>>
    suspend fun saveIntakeRecord(record: IntakeRecordDTO)
    suspend fun deleteIntakeRecord(medicationId: String, scheduleId: String, date: String)
}