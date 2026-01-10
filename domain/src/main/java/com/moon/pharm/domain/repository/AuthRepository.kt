package com.moon.pharm.domain.repository

import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun signUp(user: User): Flow<DataResourceResult<Unit>>
    suspend fun isEmailDuplicated(email: String): Boolean
}