package com.moon.pharm.data.repository

import com.google.firebase.firestore.FirebaseFirestoreException
import com.moon.pharm.data.datasource.UserDataSource
import com.moon.pharm.data.datasource.remote.dto.toDomain
import com.moon.pharm.data.datasource.remote.dto.toDto
import com.moon.pharm.data.di.IoDispatcher
import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.model.auth.UserException
import com.moon.pharm.domain.model.auth.UserLifeStyle
import com.moon.pharm.domain.repository.UserRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dataSource: UserDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : UserRepository {

    private fun Throwable.toUserException(): UserException = when {
        this is FirebaseFirestoreException && this.code == FirebaseFirestoreException.Code.NOT_FOUND -> UserException.NotFound()
        this is FirebaseFirestoreException -> UserException.NetworkError()
        else -> UserException.Unknown(this.message)
    }

    private suspend fun <T> wrapUserOperation(
        operation: suspend () -> T
    ): DataResourceResult<T> = withContext(ioDispatcher) {
        runCatching {
            operation()
        }.fold(
            onSuccess = { DataResourceResult.Success(it) },
            onFailure = { e -> DataResourceResult.Failure(e.toUserException()) }
        )
    }

    private fun <T> Flow<T>.asDataResourceResult(): Flow<DataResourceResult<T>> {
        return this
            .map { DataResourceResult.Success(it) as DataResourceResult<T> }
            .onStart { emit(DataResourceResult.Loading) }
            .catch { e -> emit(DataResourceResult.Failure(e.toUserException())) }
            .flowOn(ioDispatcher)
    }

    override suspend fun saveUser(user: User): DataResourceResult<Unit> =
        wrapUserOperation {
            dataSource.saveUser(user.toDto())
        }

    override suspend fun saveUserLifeStyle(userId: String, lifeStyle: UserLifeStyle): DataResourceResult<Unit> =
        wrapUserOperation {
            dataSource.saveUserLifeStyle(userId, lifeStyle.toDto())
        }

    override suspend fun isEmailDuplicated(email: String): Boolean {
        return withContext(ioDispatcher) { dataSource.isEmailDuplicated(email) }
    }

    override fun getUser(userId: String): Flow<DataResourceResult<User>> {
        return dataSource.getUserById(userId)
            .map { dto -> dto.toDomain() }
            .asDataResourceResult()
    }

    override suspend fun getFcmToken(): String {
        return withContext(ioDispatcher) { dataSource.getFcmToken() }
    }

    override suspend fun updateFcmToken(userId: String, token: String): DataResourceResult<Unit> =
        wrapUserOperation {
            dataSource.updateFcmToken(userId, token)
        }
}