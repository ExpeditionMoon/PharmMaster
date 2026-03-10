package com.moon.pharm.data.repository

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.moon.pharm.data.datasource.AuthDataSource
import com.moon.pharm.data.di.IoDispatcher
import com.moon.pharm.domain.model.auth.AuthException
import com.moon.pharm.domain.repository.AuthRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataSource: AuthDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AuthRepository {

    private suspend fun <T> wrapAuthOperation(
        operation: suspend () -> T
    ): DataResourceResult<T> = withContext(ioDispatcher) {
        withTimeout(10000L) {
            runCatching {
                operation()
            }.fold(
                onSuccess = { DataResourceResult.Success(it) },
                onFailure = { e ->
                    val domainError = when (e) {
                        is FirebaseAuthInvalidCredentialsException -> AuthException.InvalidCredentials()
                        is FirebaseAuthUserCollisionException -> AuthException.EmailDuplicated()
                        is FirebaseNetworkException -> AuthException.NetworkError()
                        else -> AuthException.Unknown(e.message)
                    }
                    DataResourceResult.Failure(domainError)
                }
            )
        }
    }

    override suspend fun createAccount(
        email: String, password: String
    ): DataResourceResult<String> = wrapAuthOperation {
        dataSource.createAccount(email, password)
    }

    override suspend fun login(email: String, password: String): DataResourceResult<String> =
        wrapAuthOperation {
            dataSource.login(email, password)
        }

    override suspend fun logout(): DataResourceResult<Unit> = wrapAuthOperation {
        dataSource.logout()
    }

    override suspend fun deleteAccount(): DataResourceResult<Unit> = wrapAuthOperation {
        dataSource.deleteAccount()
    }

    override fun getCurrentUserId(): String? = dataSource.getCurrentUserId()

    override suspend fun sendPasswordResetEmail(email: String): DataResourceResult<Unit> =
        wrapAuthOperation {
            dataSource.sendPasswordResetEmail(email)
        }
}