package com.moon.pharm.data.datasource

import com.moon.pharm.data.datasource.remote.dto.PharmacyDTO
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow

interface PharmacySearchDataSource {
    fun searchExternalPharmacies(query: String): Flow<DataResourceResult<List<PharmacyDTO>>>
    fun searchNearbyPharmacies(lat: Double, lng: Double): Flow<DataResourceResult<List<PharmacyDTO>>>
}