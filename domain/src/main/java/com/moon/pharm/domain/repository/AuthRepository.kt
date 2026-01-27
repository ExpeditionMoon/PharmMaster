package com.moon.pharm.domain.repository

import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun createAccount(email: String, password: String): DataResourceResult<String>

    fun login(email: String, password: String): Flow<DataResourceResult<String>>
    fun logout(): Flow<DataResourceResult<Unit>>
    fun deleteAccount(): Flow<DataResourceResult<Unit>>
    fun getCurrentUserId(): String?
    suspend fun sendPasswordResetEmail(email: String): DataResourceResult<Unit>
}