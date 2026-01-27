package com.moon.pharm.data.repository

import com.moon.pharm.data.datasource.AuthDataSource
import com.moon.pharm.data.di.IoDispatcher
import com.moon.pharm.domain.repository.AuthRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataSource: AuthDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AuthRepository {

    override suspend fun createAccount(email: String, password: String):DataResourceResult<String> =
        withContext(ioDispatcher) {
        dataSource.createAccount(email, password)
    }

    override suspend fun login(email: String, password: String): DataResourceResult<String> =
        withContext(ioDispatcher) {
            dataSource.login(email, password)
        }

    override suspend fun logout(): DataResourceResult<Unit> =
        withContext(ioDispatcher) {
            dataSource.logout()
        }

    override suspend fun deleteAccount(): DataResourceResult<Unit> =
        withContext(ioDispatcher) {
            dataSource.deleteAccount()
        }

    override fun getCurrentUserId(): String? = dataSource.getCurrentUserId()

    override suspend fun sendPasswordResetEmail(email: String): DataResourceResult<Unit> {
        return dataSource.sendPasswordResetEmail(email)
    }
}