package com.moon.pharm.data.repository

import com.moon.pharm.data.datasource.ConsultDataSource
import com.moon.pharm.data.di.IoDispatcher
import com.moon.pharm.domain.model.Pharmacist
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class ConsultRepositoryImpl @Inject constructor(
    private val dataSource: ConsultDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ConsultRepository {

    override fun getConsultItems() = dataSource.getConsultItems().map { consultList ->
            DataResourceResult.Success(consultList) as DataResourceResult<List<ConsultItem>>
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

    override fun createConsult(consultInfo: ConsultItem): Flow<DataResourceResult<Unit>> =
        wrapCUDOperation {
            dataSource.create(consultInfo)
        }

    override fun getConsultDetail(id: String): Flow<ConsultItem> = dataSource.getConsultDetail(id)

    override fun getPharmacistsByPharmacy(pharmacyId: String): List<Pharmacist> {
        return emptyList()
    }

    override fun getPharmacistById(id: String): Pharmacist? {
        return null
    }
}