package com.moon.pharm.data.datasource

import com.moon.pharm.data.datasource.remote.dto.UserDTO
import com.moon.pharm.data.datasource.remote.dto.UserLifeStyleDTO
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    suspend fun saveUser(userDto: UserDTO): DataResourceResult<Unit>
    suspend fun saveUserLifeStyle(userId: String, lifeStyleDto: UserLifeStyleDTO): DataResourceResult<Unit>
    suspend fun isEmailDuplicated(email: String): Boolean

    fun getUserById(userId: String): Flow<DataResourceResult<UserDTO>>
    suspend fun getFcmToken(): String
    suspend fun updateFcmToken(userId: String, token: String): DataResourceResult<Unit>
}