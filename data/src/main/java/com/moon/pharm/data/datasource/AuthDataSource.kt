package com.moon.pharm.data.datasource

import com.moon.pharm.domain.result.DataResourceResult

interface AuthDataSource {
    fun getCurrentUserId(): String?
    fun isUserLoggedIn(): Boolean
    suspend fun createAccount(email: String, password: String): DataResourceResult<String>
    suspend fun login(email: String, password: String): DataResourceResult<String>
    suspend fun logout(): DataResourceResult<Unit>
    suspend fun deleteAccount(): DataResourceResult<Unit>
    suspend fun sendPasswordResetEmail(email: String): DataResourceResult<Unit>
}