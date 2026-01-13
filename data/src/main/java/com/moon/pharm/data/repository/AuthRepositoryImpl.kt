package com.moon.pharm.data.repository

import com.moon.pharm.data.datasource.AuthDataSource
import com.moon.pharm.data.di.IoDispatcher
import com.moon.pharm.data.mapper.toDomain
import com.moon.pharm.data.mapper.toDto
import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.model.auth.UserLifeStyle
import com.moon.pharm.domain.repository.AuthRepository
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.result.mapResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataSource: AuthDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AuthRepository {

    override fun signUp(user: User, password: String, pharmacist: Pharmacist?): Flow<DataResourceResult<Unit>> = flow {
        emit(DataResourceResult.Loading)

        val result = dataSource.signUp(user.email, password)

        if (result is DataResourceResult.Success) {
            val uid = result.resultData

            val finalUser = user.copy(id = uid)

            val saveResult = dataSource.saveUser(finalUser.toDto())
            if (saveResult is DataResourceResult.Failure) {
                emit(DataResourceResult.Failure(saveResult.exception))
                return@flow
            }

            val defaultLifeStyle = UserLifeStyle(userId = uid)
            dataSource.saveUserLifeStyle(uid, defaultLifeStyle.toDto())

            if (pharmacist != null) {
                val finalPharmacist = pharmacist.copy(userId = uid)
                dataSource.savePharmacist(finalPharmacist.toDto())
            }
            emit(DataResourceResult.Success(Unit))
        } else if (result is DataResourceResult.Failure) {
            emit(DataResourceResult.Failure(result.exception))
        }
    }.catch { e ->
        emit(DataResourceResult.Failure(e))
    }.flowOn(ioDispatcher)

    override suspend fun isEmailDuplicated(email: String): Boolean {
        return withContext(ioDispatcher) {
            dataSource.isEmailDuplicated(email)
        }
    }

    override fun getPharmacistById(pharmacistId: String): Flow<DataResourceResult<Pharmacist>> {
        val pharmacistFlow = dataSource.getPharmacistById(pharmacistId)

        val userFlow = dataSource.getUserById(pharmacistId)

        return pharmacistFlow.combine(userFlow) { pharmacistResult, userResult ->

            if (pharmacistResult is DataResourceResult.Success && userResult is DataResourceResult.Success) {
                val pDto = pharmacistResult.resultData
                DataResourceResult.Success(pDto.toDomain())
            }
            else if (pharmacistResult is DataResourceResult.Loading || userResult is DataResourceResult.Loading) {
                DataResourceResult.Loading
            }
            else {
                val exception = (pharmacistResult as? DataResourceResult.Failure)?.exception
                    ?: (userResult as? DataResourceResult.Failure)?.exception
                    ?: Exception("Unknown error")
                DataResourceResult.Failure(exception)
            }
        }.flowOn(ioDispatcher)
    }

    override fun getPharmacistsByPlaceId(placeId: String): Flow<DataResourceResult<List<Pharmacist>>> {
        return dataSource.getPharmacistsByPlaceId(placeId).map { result ->
            result.mapResult { dtos ->
                dtos.map { dto ->
                    dto.toDomain()
                }
            }
        }.flowOn(ioDispatcher)
    }

    override fun getCurrentUserId(): String? {
        return dataSource.getCurrentUserId()
    }
}