package com.moon.pharm.domain.repository

import com.moon.pharm.domain.model.pharmacy.Pharmacy
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow

interface PharmacyRepository {
    fun searchPharmacies(query: String): Flow<DataResourceResult<List<Pharmacy>>>
    fun searchNearbyPharmacies(lat: Double, lng: Double): Flow<DataResourceResult<List<Pharmacy>>>
}