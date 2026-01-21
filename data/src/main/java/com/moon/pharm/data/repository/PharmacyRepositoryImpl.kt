package com.moon.pharm.data.repository

import com.moon.pharm.data.datasource.PharmacySearchDataSource
import com.moon.pharm.data.datasource.PharmacyStorageDataSource
import com.moon.pharm.data.mapper.toDomain
import com.moon.pharm.data.mapper.toDto
import com.moon.pharm.domain.model.pharmacy.Pharmacy
import com.moon.pharm.domain.repository.PharmacyRepository
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.result.mapResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PharmacyRepositoryImpl @Inject constructor(
    private val pharmacyStorageDataSource: PharmacyStorageDataSource,
    private val pharmacySearchDataSource: PharmacySearchDataSource
) : PharmacyRepository {

    override fun searchPharmacies(query: String): Flow<DataResourceResult<List<Pharmacy>>> {
        return pharmacySearchDataSource.searchExternalPharmacies(query).map { result ->
            result.mapResult { dtoList ->
                dtoList.map { it.toDomain() }
            }
        }
    }

    override fun searchNearbyPharmacies(lat: Double, lng: Double): Flow<DataResourceResult<List<Pharmacy>>> {
        return pharmacySearchDataSource.searchNearbyPharmacies(lat, lng).map { result ->
            result.mapResult { dtoList ->
                dtoList.map { it.toDomain() }
            }
        }
    }

    override suspend fun savePharmacy(pharmacy: Pharmacy): DataResourceResult<Unit> {
        return pharmacyStorageDataSource.savePharmacyToFirestore(pharmacy.toDto())
    }
}