package com.moon.pharm.data.datasource

import com.moon.pharm.data.datasource.remote.dto.PharmacyDTO
import kotlinx.coroutines.flow.Flow

interface PharmacySearchDataSource {
    fun searchExternalPharmacies(query: String): Flow<List<PharmacyDTO>>
    fun searchNearbyPharmacies(lat: Double, lng: Double): Flow<List<PharmacyDTO>>
}