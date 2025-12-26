package com.moon.pharm.data.impl

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

class ConsultRepositoryImpl(
    private val dataSource: ConsultDataSource
) : ConsultRepository {
    override fun createConsult(consultInfo: ConsultItem): Flow<DataResourceResult<Unit>> = flow {
        emit(DataResourceResult.Loading)
        dataSource.create(consultInfo)
        emit(DataResourceResult.Success(Unit))
    }.catch { e ->
        emit(DataResourceResult.Failure(e))
    }.flowOn(Dispatchers.IO)

    override fun getConsultItems(): Flow<DataResourceResult<List<ConsultItem>>> = flow {
        emit(DataResourceResult.Success(emptyList()))
    }

    override fun getConsultDetail(id: String): Flow<ConsultItem> = flow {
        emit(
            ConsultItem(
                id = id,
                title = "불러오는 중...",
                userId = "",
                content = "",
                expertId = "",
                status = ConsultStatus.WAITING,
                createdAt = System.currentTimeMillis().toString(),
                images = emptyList(),
                answer = null
            )
        )
    }

    override fun getPharmacistsByPharmacy(pharmacyId: String): List<Pharmacist> {
        return emptyList()
    }

    override fun getPharmacistById(id: String): Pharmacist? {
        return null
    }
}