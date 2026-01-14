package com.moon.pharm.domain.repository

import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow

interface PharmacistRepository {
    suspend fun savePharmacist(pharmacist: Pharmacist): DataResourceResult<Unit>

    fun getPharmacistById(pharmacistId: String): Flow<DataResourceResult<Pharmacist>>
    fun getPharmacistsByPlaceId(placeId: String): Flow<DataResourceResult<List<Pharmacist>>>
}