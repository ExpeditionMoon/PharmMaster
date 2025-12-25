package com.moon.pharm.data.repository

import com.moon.pharm.data.datasource.ConsultDataSource
import com.moon.pharm.domain.model.ConsultItem
import com.moon.pharm.domain.model.ConsultStatus
import com.moon.pharm.domain.model.Pharmacist
import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FirestoreConsultRepositoryImpl(
    private val dataSource: ConsultDataSource
) : ConsultRepository {
    private fun wrapCUDOperation(
        operation: suspend () -> Unit
    ): Flow<DataResourceResult<Unit>> = flow {
        emit(DataResourceResult.Loading)
        operation()
        emit(DataResourceResult.Success(Unit))
    }.catch { e ->
        emit(DataResourceResult.Failure(e))
    }.flowOn(Dispatchers.IO)

    override fun createConsult(consultInfo: ConsultItem): Flow<DataResourceResult<Unit>> = wrapCUDOperation {
        dataSource.create(consultInfo)
    }

    override fun getConsultItems(): Flow<DataResourceResult<List<ConsultItem>>> {
        return dataSource.getConsultItems()
    }

    override fun getConsultDetail(id: String): Flow<ConsultItem> = flow {
        emit(ConsultItem(
            id = id,
            userId = "사용자",
            expertId = null,
            title = "로딩 중...",
            content = "내용을 불러오고 있습니다.",
            status = ConsultStatus.WAITING,
            createdAt = "2023-01-01"
        ))
    }

    override fun getPharmacistsByPharmacy(pharmacyId: String): List<Pharmacist> {
        return emptyList()
    }

    override fun getPharmacistById(id: String): Pharmacist? {
        return null
    }
}