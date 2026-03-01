package com.moon.pharm.data.repository

import com.google.firebase.firestore.FirebaseFirestoreException
import com.moon.pharm.data.datasource.MedicationDataSource
import com.moon.pharm.data.di.IoDispatcher
import com.moon.pharm.data.mapper.toDomain
import com.moon.pharm.data.mapper.toDto
import com.moon.pharm.domain.model.medication.IntakeRecord
import com.moon.pharm.domain.model.medication.Medication
import com.moon.pharm.domain.model.medication.MedicationException
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

    private fun Throwable.toMedicationException(): MedicationException = when {
        this is FirebaseFirestoreException && this.code == FirebaseFirestoreException.Code.NOT_FOUND -> MedicationException.NotFound()
        this is FirebaseFirestoreException -> MedicationException.NetworkError()
        else -> MedicationException.Unknown(this.message)
    }

    private fun wrapCUDOperation(
        operation: suspend () -> Unit
    ): Flow<DataResourceResult<Unit>> = flow {
        emit(DataResourceResult.Loading)
        operation()
        emit(DataResourceResult.Success(Unit))
    }.catch { e ->
        emit(DataResourceResult.Failure(e.toMedicationException()))
    }.flowOn(ioDispatcher)

    private fun <T> Flow<T>.asDataResourceResult(): Flow<DataResourceResult<T>> {
        return this
            .map { DataResourceResult.Success(it) as DataResourceResult<T> }
            .onStart { emit(DataResourceResult.Loading) }
            .catch { e ->
                emit(DataResourceResult.Failure(e.toMedicationException()))
            }
            .flowOn(ioDispatcher)
    }
    override fun getMedications(userId: String): Flow<DataResourceResult<List<Medication>>> {
        return dataSource.getMedications(userId)
            .map { dtoList -> dtoList.map { it.toDomain() } }
            .asDataResourceResult()
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
            .map { dtoList -> dtoList.map { it.toDomain() } }
            .asDataResourceResult()
    }

    override fun getIntakeRecordsByRange(
        userId: String,
        startDate: String,
        endDate: String
    ): Flow<DataResourceResult<List<IntakeRecord>>> {
        return dataSource.getIntakeRecordsByRange(userId, startDate, endDate)
            .map { dtoList -> dtoList.map { it.toDomain() } }
            .asDataResourceResult()
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