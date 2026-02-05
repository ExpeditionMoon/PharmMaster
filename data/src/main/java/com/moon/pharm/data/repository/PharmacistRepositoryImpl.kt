package com.moon.pharm.data.repository

import com.moon.pharm.data.datasource.PharmacistDataSource
import com.moon.pharm.data.di.IoDispatcher
import com.moon.pharm.data.mapper.toDomain
import com.moon.pharm.data.mapper.toDto
import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.repository.PharmacistRepository
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.result.mapResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PharmacistRepositoryImpl @Inject constructor(
    private val dataSource: PharmacistDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : PharmacistRepository {

    override suspend fun savePharmacist(pharmacist: Pharmacist): DataResourceResult<Unit> {
        return dataSource.savePharmacist(pharmacist.toDto())
    }

    override fun getPharmacistById(pharmacistId: String): Flow<DataResourceResult<Pharmacist>> {
        return dataSource.getPharmacistById(pharmacistId).map { result ->
            when (result) {
                is DataResourceResult.Success -> DataResourceResult.Success(result.resultData.toDomain())
                is DataResourceResult.Failure -> DataResourceResult.Failure(result.exception)
                is DataResourceResult.Loading -> DataResourceResult.Loading
            }
        }.flowOn(ioDispatcher)
    }

    override fun getPharmacistsByPlaceId(placeId: String): Flow<DataResourceResult<List<Pharmacist>>> {
        return dataSource.getPharmacistsByPlaceId(placeId).map { result ->
            result.mapResult { dtos ->
                dtos.map { it.toDomain() }
            }
        }.flowOn(ioDispatcher)
    }

    override suspend fun updatePharmacistNickname(userId: String, newNickname: String): DataResourceResult<Unit> {
        return dataSource.updatePharmacistNickname(userId, newNickname)
    }
}