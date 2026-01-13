package com.moon.pharm.domain.repository

import com.moon.pharm.domain.model.auth.UserLifeStyle
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow

interface UserLifeStyleRepository {
    fun getUserLifeStyle(userId: String): Flow<DataResourceResult<UserLifeStyle>>
    fun saveUserLifeStyle(userLifeStyle: UserLifeStyle): Flow<DataResourceResult<Unit>>
}