package com.moon.pharm.data.repository

import com.google.firebase.firestore.FirebaseFirestoreException
import com.moon.pharm.data.datasource.PharmacySearchDataSource
import com.moon.pharm.data.datasource.PharmacyStorageDataSource
import com.moon.pharm.data.di.IoDispatcher
import com.moon.pharm.data.mapper.toDomain
import com.moon.pharm.data.mapper.toDto
import com.moon.pharm.domain.model.pharmacy.Pharmacy
import com.moon.pharm.domain.model.pharmacy.PharmacyException
import com.moon.pharm.domain.repository.PharmacyRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PharmacyRepositoryImpl @Inject constructor(
    private val pharmacyStorageDataSource: PharmacyStorageDataSource,
    private val pharmacySearchDataSource: PharmacySearchDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : PharmacyRepository {

    private fun Throwable.toPharmacyException(): PharmacyException = when {
        this is FirebaseFirestoreException && this.code == FirebaseFirestoreException.Code.NOT_FOUND -> PharmacyException.NotFound()
        this is FirebaseFirestoreException -> PharmacyException.NetworkError()
        else -> PharmacyException.Unknown(this.message)
    }

    private suspend fun <T> wrapPharmacyOperation(
        operation: suspend () -> T
    ): DataResourceResult<T> = withContext(ioDispatcher) {
        runCatching {
            operation()
        }.fold(
            onSuccess = { DataResourceResult.Success(it) },
            onFailure = { e -> DataResourceResult.Failure(e.toPharmacyException()) }
        )
    }

    private fun <T> Flow<T>.asDataResourceResult(): Flow<DataResourceResult<T>> {
        return this
            .map { DataResourceResult.Success(it) as DataResourceResult<T> }
            .onStart { emit(DataResourceResult.Loading) }
            .catch { e -> emit(DataResourceResult.Failure(e.toPharmacyException())) }
            .flowOn(ioDispatcher)
    }

    override fun searchPharmacies(query: String): Flow<DataResourceResult<List<Pharmacy>>> {
        return pharmacySearchDataSource.searchExternalPharmacies(query)
            .map { dtoList -> dtoList.map { it.toDomain() } }
            .asDataResourceResult()
    }

    override fun searchNearbyPharmacies(lat: Double, lng: Double): Flow<DataResourceResult<List<Pharmacy>>> {
        return pharmacySearchDataSource.searchNearbyPharmacies(lat, lng)
            .map { dtoList -> dtoList.map { it.toDomain() } }
            .asDataResourceResult()
    }

    override suspend fun savePharmacy(pharmacy: Pharmacy): DataResourceResult<Unit> =
        wrapPharmacyOperation {
            pharmacyStorageDataSource.savePharmacyToFirestore(pharmacy.toDto())
        }
}