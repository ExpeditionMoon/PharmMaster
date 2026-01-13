package com.moon.pharm.domain.repository

import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun signUp(user: User, password: String, pharmacist: Pharmacist? = null): Flow<DataResourceResult<Unit>>
    suspend fun isEmailDuplicated(email: String): Boolean

    fun getPharmacistsByPlaceId(placeId: String): Flow<DataResourceResult<List<Pharmacist>>>
    fun getPharmacistById(pharmacistId: String): Flow<DataResourceResult<Pharmacist>>
    fun getCurrentUserId(): String?
}