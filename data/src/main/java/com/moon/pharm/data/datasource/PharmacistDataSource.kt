package com.moon.pharm.data.datasource

import com.moon.pharm.data.datasource.remote.dto.PharmacistDTO
import kotlinx.coroutines.flow.Flow

interface PharmacistDataSource {
    suspend fun savePharmacist(pharmacistDto: PharmacistDTO)
    fun getPharmacistById(pharmacistId: String): Flow<PharmacistDTO>
    fun getPharmacistsByPlaceId(placeId: String): Flow<List<PharmacistDTO>>
    suspend fun updatePharmacistNickname(userId: String, newNickname: String)
}