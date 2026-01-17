package com.moon.pharm.data.repository

import com.moon.pharm.data.datasource.PharmacyDataSource
import com.moon.pharm.data.mapper.toDomain
import com.moon.pharm.domain.model.pharmacy.Pharmacy
import com.moon.pharm.domain.repository.PharmacyRepository
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.result.mapResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PharmacyRepositoryImpl @Inject constructor(
    private val pharmacyDataSource: PharmacyDataSource
) : PharmacyRepository {

    override fun searchPharmacies(query: String): Flow<DataResourceResult<List<Pharmacy>>> {
        return pharmacyDataSource.searchExternalPharmacies(query).map { result ->
            result.mapResult { dto ->
                dto.map { it.toDomain() }
            }
        }
    }

    override fun searchNearbyPharmacies(lat: Double, lng: Double): Flow<DataResourceResult<List<Pharmacy>>> {
        return pharmacyDataSource.searchNearbyPharmacies(lat, lng).map { result ->
            result.mapResult { dto ->
                dto.map { it.toDomain() }
            }
        }
    }
}