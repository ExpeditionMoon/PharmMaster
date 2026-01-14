package com.moon.pharm.data.repository

import com.moon.pharm.data.datasource.AuthDataSource
import com.moon.pharm.data.di.IoDispatcher
import com.moon.pharm.domain.repository.AuthRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataSource: AuthDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AuthRepository {

    override fun createAccount(email: String, password: String): Flow<DataResourceResult<String>> = flow {
        emit(DataResourceResult.Loading)
        emit(dataSource.createAccount(email, password))
    }.flowOn(ioDispatcher)

    override fun login(email: String, password: String): Flow<DataResourceResult<String>> = flow {
        emit(DataResourceResult.Loading)
        emit(dataSource.login(email, password))
    }.flowOn(ioDispatcher)

    override fun logout(): Flow<DataResourceResult<Unit>> = flow {
        emit(DataResourceResult.Loading)
        emit(dataSource.logout())
    }.flowOn(ioDispatcher)

    override fun deleteAccount(): Flow<DataResourceResult<Unit>> = flow {
        emit(DataResourceResult.Loading)
        emit(dataSource.deleteAccount())
    }.flowOn(ioDispatcher)

    override fun getCurrentUserId(): String? = dataSource.getCurrentUserId()

    override suspend fun sendPasswordResetEmail(email: String): DataResourceResult<Unit> {
        return dataSource.sendPasswordResetEmail(email)
    }
}