package com.moon.pharm.data.repository

import com.google.firebase.firestore.FirebaseFirestoreException
import com.moon.pharm.data.datasource.PharmacySearchDataSource
import com.moon.pharm.data.datasource.PharmacyStorageDataSource
import com.moon.pharm.data.datasource.remote.dto.toDomain
import com.moon.pharm.data.datasource.remote.dto.toDto
import com.moon.pharm.data.di.IoDispatcher
import com.moon.pharm.domain.model.pharmacy.Pharmacy
import com.moon.pharm.domain.model.pharmacy.PharmacyException
import com.moon.pharm.domain.repository.PharmacyRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PharmacyRepositoryImpl @Inject constructor(
    private val pharmacyStorageDataSource: PharmacyStorageDataSource,
    private val pharmacySearchDataSource: PharmacySearchDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : PharmacyRepository {

    private fun Throwable.toPharmacyException(): PharmacyException = when {
        this is PharmacyException -> this
        this is FirebaseFirestoreException && this.code == FirebaseFirestoreException.Code.NOT_FOUND -> PharmacyException.NotFound()
        this is FirebaseFirestoreException -> PharmacyException.NetworkError()
        else -> PharmacyException.Unknown(message)
    }

    private suspend fun <T> wrapPharmacyOperation(operation: suspend () -> T): DataResourceResult<T> =
        runDataResourceOperation(
            dispatcher = ioDispatcher,
            errorMapper = { it.toPharmacyException() },
            operation = operation
        )

    private fun <T> Flow<T>.asDataResourceResult(): Flow<DataResourceResult<T>> {
        return asDataResourceResult(
            dispatcher = ioDispatcher,
            errorMapper = { it.toPharmacyException() }
        )
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
