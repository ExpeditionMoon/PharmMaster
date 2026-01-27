package com.moon.pharm.data.datasource

import com.moon.pharm.data.datasource.remote.dto.PharmacyDTO
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow

interface PharmacyStorageDataSource {
    fun getPharmacyFromFirestore(placeId: String): Flow<DataResourceResult<PharmacyDTO>>
    suspend fun savePharmacyToFirestore(pharmacyDTO: PharmacyDTO): DataResourceResult<Unit>
}