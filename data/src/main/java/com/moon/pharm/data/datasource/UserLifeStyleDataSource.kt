package com.moon.pharm.data.datasource

import com.moon.pharm.data.datasource.remote.dto.UserLifeStyleDTO
import kotlinx.coroutines.flow.Flow

interface UserLifeStyleDataSource {
    fun getUserLifeStyle(userId: String): Flow<UserLifeStyleDTO?>
    suspend fun saveUserLifeStyle(userId: String, dto: UserLifeStyleDTO)
}