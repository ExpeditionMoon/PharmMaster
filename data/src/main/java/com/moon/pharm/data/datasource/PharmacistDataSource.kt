package com.moon.pharm.data.datasource

import com.moon.pharm.data.datasource.remote.dto.PharmacistDTO
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow

interface PharmacistDataSource {
    suspend fun savePharmacist(pharmacistDto: PharmacistDTO): DataResourceResult<Unit>
    fun getPharmacistById(pharmacistId: String): Flow<DataResourceResult<PharmacistDTO>>
    fun getPharmacistsByPlaceId(placeId: String): Flow<DataResourceResult<List<PharmacistDTO>>>
}