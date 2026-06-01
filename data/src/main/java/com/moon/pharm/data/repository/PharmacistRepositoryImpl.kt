package com.moon.pharm.data.repository

import com.google.firebase.firestore.FirebaseFirestoreException
import com.moon.pharm.data.datasource.PharmacistDataSource
import com.moon.pharm.data.datasource.remote.dto.toDomain
import com.moon.pharm.data.datasource.remote.dto.toDto
import com.moon.pharm.data.di.IoDispatcher
import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.model.auth.PharmacistException
import com.moon.pharm.domain.repository.PharmacistRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PharmacistRepositoryImpl @Inject constructor(
    private val dataSource: PharmacistDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : PharmacistRepository {

    private fun Throwable.toPharmacistException(): PharmacistException = when {
        this is PharmacistException -> this
        this is FirebaseFirestoreException && this.code == FirebaseFirestoreException.Code.NOT_FOUND -> PharmacistException.NotFound()
        this is FirebaseFirestoreException -> PharmacistException.NetworkError()
        else -> PharmacistException.Unknown(message)
    }

    private suspend fun <T> wrapPharmacistOperation(operation: suspend () -> T): DataResourceResult<T> =
        runDataResourceOperation(
            dispatcher = ioDispatcher,
            errorMapper = { it.toPharmacistException() },
            operation = operation
        )

    private fun <T> Flow<T>.asDataResourceResult(): Flow<DataResourceResult<T>> {
        return asDataResourceResult(
            dispatcher = ioDispatcher,
            errorMapper = { it.toPharmacistException() }
        )
    }

    override suspend fun savePharmacist(pharmacist: Pharmacist): DataResourceResult<Unit> =
        wrapPharmacistOperation {
            dataSource.savePharmacist(pharmacist.toDto())
        }

    override fun getPharmacistById(pharmacistId: String): Flow<DataResourceResult<Pharmacist>> {
        return dataSource.getPharmacistById(pharmacistId)
            .map { dto -> dto.toDomain() }
            .asDataResourceResult()
    }

    override fun getPharmacistsByPlaceId(placeId: String): Flow<DataResourceResult<List<Pharmacist>>> {
        return dataSource.getPharmacistsByPlaceId(placeId)
            .map { dtoList -> dtoList.map { it.toDomain() } }
            .asDataResourceResult()
    }

    override suspend fun updatePharmacistNickname(userId: String, newNickname: String): DataResourceResult<Unit> =
        wrapPharmacistOperation {
            dataSource.updatePharmacistNickname(userId, newNickname)
        }
}
