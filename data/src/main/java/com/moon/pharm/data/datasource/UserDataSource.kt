package com.moon.pharm.data.datasource

import com.moon.pharm.data.datasource.remote.dto.UserDTO
import com.moon.pharm.data.datasource.remote.dto.UserLifeStyleDTO
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    suspend fun saveUser(userDto: UserDTO)
    suspend fun saveUserLifeStyle(userId: String, lifeStyleDto: UserLifeStyleDTO)
    suspend fun isEmailDuplicated(email: String): Boolean
    fun getUserById(userId: String): Flow<UserDTO>
    suspend fun getFcmToken(): String
    suspend fun updateFcmToken(userId: String, token: String)
}