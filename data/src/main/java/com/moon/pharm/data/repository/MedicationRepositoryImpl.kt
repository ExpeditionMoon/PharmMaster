package com.moon.pharm.data.repository

import com.moon.pharm.data.datasource.MedicationDataSource
import com.moon.pharm.data.di.IoDispatcher
import com.moon.pharm.data.mapper.toDomain
import com.moon.pharm.data.mapper.toDto
import com.moon.pharm.domain.model.medication.IntakeRecord
import com.moon.pharm.domain.model.medication.Medication
import com.moon.pharm.domain.repository.MedicationRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class MedicationRepositoryImpl @Inject constructor(
    private val dataSource: MedicationDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : MedicationRepository {

    private fun wrapCUDOperation(
        operation: suspend () -> Unit
    ): Flow<DataResourceResult<Unit>> = flow {
        emit(DataResourceResult.Loading)
        operation()
        emit(DataResourceResult.Success(Unit))
    }.catch { e ->
        emit(DataResourceResult.Failure(e))
    }.flowOn(ioDispatcher)

    override fun getMedications(userId: String): Flow<DataResourceResult<List<Medication>>> {
        return dataSource.getMedications(userId)
            .map { dto ->
                val medications = dto.map { it.toDomain() }
                DataResourceResult.Success(medications) as DataResourceResult<List<Medication>>
            }
            .onStart { emit(DataResourceResult.Loading) }
            .catch { e -> emit(DataResourceResult.Failure(e)) }
            .flowOn(ioDispatcher)
    }

    override fun saveMedication(medication: Medication): Flow<DataResourceResult<Unit>> =
        wrapCUDOperation {
            dataSource.saveMedication(medication.toDto())
        }

    override fun deleteMedication(medicationId: String): Flow<DataResourceResult<Unit>> =
        wrapCUDOperation {
            dataSource.deleteMedication(medicationId)
        }

    override fun getIntakeRecords(userId: String, date: String): Flow<DataResourceResult<List<IntakeRecord>>> {
        return dataSource.getIntakeRecords(userId, date)
            .map { dto ->
                val records = dto.map { it.toDomain() }
                DataResourceResult.Success(records) as DataResourceResult<List<IntakeRecord>>
            }
            .onStart { emit(DataResourceResult.Loading) }
            .catch { e -> emit(DataResourceResult.Failure(e)) }
            .flowOn(ioDispatcher)
    }

    override fun getIntakeRecordsByRange(
        userId: String,
        startDate: String,
        endDate: String
    ): Flow<DataResourceResult<List<IntakeRecord>>> {
        return dataSource.getIntakeRecordsByRange(userId, startDate, endDate)
            .map { dto ->
                val records = dto.map { it.toDomain() }
                DataResourceResult.Success(records) as DataResourceResult<List<IntakeRecord>>
            }
            .onStart { emit(DataResourceResult.Loading) }
            .catch { e ->
                emit(DataResourceResult.Failure(e)) }
            .flowOn(ioDispatcher)
    }

    override fun saveIntakeRecord(record: IntakeRecord): Flow<DataResourceResult<Unit>> =
        wrapCUDOperation {
            dataSource.saveIntakeRecord(record.toDto())
        }

    override fun deleteIntakeRecord(
        medicationId: String,
        scheduleId: String,
        date: String
    ): Flow<DataResourceResult<Unit>> = wrapCUDOperation {
        dataSource.deleteIntakeRecord(medicationId, scheduleId, date)
    }
}