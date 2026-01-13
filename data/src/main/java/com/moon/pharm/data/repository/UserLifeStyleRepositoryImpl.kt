package com.moon.pharm.data.repository

import com.moon.pharm.data.datasource.UserLifeStyleDataSource
import com.moon.pharm.data.di.IoDispatcher
import com.moon.pharm.data.mapper.toDomain
import com.moon.pharm.data.mapper.toDto
import com.moon.pharm.domain.model.auth.UserLifeStyle
import com.moon.pharm.domain.repository.UserLifeStyleRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserLifeStyleRepositoryImpl @Inject constructor(
    private val dataSource: UserLifeStyleDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : UserLifeStyleRepository {

    private fun wrapCUDOperation(
        operation: suspend () -> Unit
    ): Flow<DataResourceResult<Unit>> = flow {
        emit(DataResourceResult.Loading)
        operation()
        emit(DataResourceResult.Success(Unit))
    }.catch { e ->
        emit(DataResourceResult.Failure(e))
    }.flowOn(ioDispatcher)

    override fun getUserLifeStyle(userId: String): Flow<DataResourceResult<UserLifeStyle>> {
        return dataSource.getUserLifeStyle(userId)
            .map { dto ->
                val domainModel = dto?.toDomain() ?: UserLifeStyle(userId = userId)
                DataResourceResult.Success(domainModel) as DataResourceResult<UserLifeStyle>
            }
            .catch { e ->
                emit(DataResourceResult.Failure(e))
            }
            .flowOn(ioDispatcher)
    }

    override fun saveUserLifeStyle(userLifeStyle: UserLifeStyle): Flow<DataResourceResult<Unit>> =
        wrapCUDOperation {
            dataSource.saveUserLifeStyle(userLifeStyle.userId, userLifeStyle.toDto())
        }
}