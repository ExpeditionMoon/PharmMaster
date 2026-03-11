package com.moon.pharm.data.datasource

interface AuthDataSource {
    fun getCurrentUserId(): String?
    fun isUserLoggedIn(): Boolean
    suspend fun createAccount(email: String, password: String): String
    suspend fun login(email: String, password: String): String
    suspend fun logout()
    suspend fun deleteAccount()
    suspend fun sendPasswordResetEmail(email: String)
}