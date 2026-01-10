package com.moon.pharm.data.datasource

import com.moon.pharm.domain.model.auth.User

interface AuthDataSource{
    suspend fun createUser(user: User)
    suspend fun isEmailDuplicated(email: String): Boolean
    suspend fun signUp(email: String): String
}