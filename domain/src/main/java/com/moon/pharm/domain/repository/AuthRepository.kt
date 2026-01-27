package com.moon.pharm.domain.repository

import com.moon.pharm.domain.result.DataResourceResult

interface AuthRepository {
    suspend fun createAccount(email: String, password: String): DataResourceResult<String>
    suspend fun login(email: String, password: String): DataResourceResult<String>
    suspend fun logout(): DataResourceResult<Unit>
    suspend fun deleteAccount(): DataResourceResult<Unit>
    fun getCurrentUserId(): String?
    suspend fun sendPasswordResetEmail(email: String): DataResourceResult<Unit>
}