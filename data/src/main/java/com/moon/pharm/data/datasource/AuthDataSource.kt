package com.moon.pharm.data.datasource

import com.moon.pharm.data.datasource.remote.dto.PharmacistDTO
import com.moon.pharm.data.datasource.remote.dto.UserDTO
import com.moon.pharm.data.datasource.remote.dto.UserLifeStyleDTO
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow

interface AuthDataSource {
    fun getCurrentUserId(): String?
    fun isUserLoggedIn(): Boolean
    suspend fun signUp(email: String, password: String): DataResourceResult<String>
    suspend fun login(email: String, password: String): DataResourceResult<String>
    suspend fun logout(): DataResourceResult<Unit>
    suspend fun deleteAccount(): DataResourceResult<Unit>
    suspend fun sendPasswordResetEmail(email: String): DataResourceResult<Unit>
    suspend fun isEmailDuplicated(email: String): Boolean

    suspend fun saveUser(userDto: UserDTO): DataResourceResult<Unit>
    suspend fun saveUserLifeStyle(userId: String, lifeStyleDto: UserLifeStyleDTO): DataResourceResult<Unit>
    suspend fun savePharmacist(pharmacistDto: PharmacistDTO): DataResourceResult<Unit>

    fun getUserById(userId: String): Flow<DataResourceResult<UserDTO>>
    fun getPharmacistsByPlaceId(placeId: String): Flow<DataResourceResult<List<PharmacistDTO>>>
    fun getPharmacistById(pharmacistId: String): Flow<DataResourceResult<PharmacistDTO>>
}