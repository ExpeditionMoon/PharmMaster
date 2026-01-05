package com.moon.pharm.domain.repository

import com.moon.pharm.domain.model.User
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun signUp(user: User): Flow<DataResourceResult<Unit>>
}