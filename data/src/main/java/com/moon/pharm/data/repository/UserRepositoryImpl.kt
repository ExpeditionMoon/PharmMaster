package com.moon.pharm.data.repository

import com.moon.pharm.data.datasource.UserDataSource
import com.moon.pharm.data.di.IoDispatcher
import com.moon.pharm.data.mapper.toDomain
import com.moon.pharm.data.mapper.toDto
import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.model.auth.UserLifeStyle
import com.moon.pharm.domain.repository.UserRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dataSource: UserDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : UserRepository {

    override suspend fun saveUser(user: User): DataResourceResult<Unit> {
        return dataSource.saveUser(user.toDto())
    }

    override suspend fun saveUserLifeStyle(userId: String, lifeStyle: UserLifeStyle): DataResourceResult<Unit> {
        return dataSource.saveUserLifeStyle(userId, lifeStyle.toDto())
    }

    override suspend fun isEmailDuplicated(email: String): Boolean {
        return dataSource.isEmailDuplicated(email)
    }

    override fun getUser(userId: String): Flow<DataResourceResult<User>> {
        return dataSource.getUserById(userId).map { result ->
            when (result) {
                is DataResourceResult.Success -> DataResourceResult.Success(result.resultData.toDomain())
                is DataResourceResult.Failure -> DataResourceResult.Failure(result.exception)
                is DataResourceResult.Loading -> DataResourceResult.Loading
            }
        }.flowOn(ioDispatcher)
    }
}