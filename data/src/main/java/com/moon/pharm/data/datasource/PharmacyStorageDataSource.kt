package com.moon.pharm.data.datasource

import com.moon.pharm.data.datasource.remote.dto.PharmacyDTO
import kotlinx.coroutines.flow.Flow

interface PharmacyStorageDataSource {
    fun getPharmacyFromFirestore(placeId: String): Flow<PharmacyDTO>
    suspend fun savePharmacyToFirestore(pharmacyDTO: PharmacyDTO)
}