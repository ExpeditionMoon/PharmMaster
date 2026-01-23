package com.moon.pharm.data.repository

import com.moon.pharm.data.datasource.ConsultDataSource
import com.moon.pharm.data.datasource.ImageDataSource
import com.moon.pharm.data.di.IoDispatcher
import com.moon.pharm.domain.model.consult.ConsultError
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
    private val imageDataSource: ImageDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ConsultRepository {

    private fun wrapCUDOperation(
        operation: suspend () -> Unit
    ): Flow<DataResourceResult<Unit>> = flow {
        emit(DataResourceResult.Loading)
        operation()
        emit(DataResourceResult.Success(Unit))
    }.catch { e ->
        emit(DataResourceResult.Failure(e))
    }.flowOn(ioDispatcher)

    override fun getConsultItems() = dataSource.getConsultItems()
        .map { list ->
            DataResourceResult.Success(list) as DataResourceResult<List<ConsultItem>>
        }
        .onStart { emit(DataResourceResult.Loading) }
        .catch { emit(DataResourceResult.Failure(it)) }
        .flowOn(ioDispatcher)

    override fun createConsult(consultInfo: ConsultItem) =
        wrapCUDOperation { dataSource.create(consultInfo) }

    override fun getConsultDetail(id: String): Flow<DataResourceResult<ConsultItem>> {
        return dataSource.getConsultDetail(id)
            .map { item ->
                if (item != null) {
                    DataResourceResult.Success(item) as DataResourceResult<ConsultItem>
                } else {
                    DataResourceResult.Failure(ConsultError.NotFound())
                }
            }
            .onStart { emit(DataResourceResult.Loading) }
            .catch { e -> emit(DataResourceResult.Failure(e)) }
            .flowOn(ioDispatcher)
    }

    override suspend fun uploadImage(uri: String, userId: String): String {
        return imageDataSource.uploadImage(uri, userId)
    }
}