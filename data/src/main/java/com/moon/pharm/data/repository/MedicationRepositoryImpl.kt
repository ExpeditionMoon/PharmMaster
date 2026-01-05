package com.moon.pharm.data.repository

import com.moon.pharm.data.datasource.MedicationDataSource
import com.moon.pharm.data.di.IoDispatcher
import com.moon.pharm.domain.model.MedicationItem
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

    override fun getMedicationItems() = dataSource.getMedicationItems().map { medicationList ->
        DataResourceResult.Success(medicationList) as DataResourceResult<List<MedicationItem>>
    }.catch { e ->
        emit(DataResourceResult.Failure(e))
    }.onStart { emit(DataResourceResult.Loading) }.flowOn(ioDispatcher)


    private fun wrapCUDOperation(
        operation: suspend () -> Unit
    ): Flow<DataResourceResult<Unit>> = flow {
        emit(DataResourceResult.Loading)
        operation()
        emit(DataResourceResult.Success(Unit))
    }.catch { e ->
        emit(DataResourceResult.Failure(e))
    }.flowOn(ioDispatcher)

    override fun createMedication(medicationItem: MedicationItem): Flow<DataResourceResult<Unit>> =
        wrapCUDOperation {
            dataSource.create(medicationItem)
    }

}