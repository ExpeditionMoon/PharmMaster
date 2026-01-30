package com.moon.pharm.domain.repository

import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.model.auth.UserLifeStyle
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun saveUser(user: User): DataResourceResult<Unit>
    suspend fun saveUserLifeStyle(userId: String, lifeStyle: UserLifeStyle): DataResourceResult<Unit>
    suspend fun isEmailDuplicated(email: String): Boolean

    fun getUser(userId: String): Flow<DataResourceResult<User>>
    suspend fun getFcmToken(): String
    suspend fun updateFcmToken(userId: String, token: String): DataResourceResult<Unit>
}