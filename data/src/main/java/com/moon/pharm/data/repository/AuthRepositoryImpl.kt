package com.moon.pharm.data.repository

import com.moon.pharm.data.datasource.AuthDataSource
import com.moon.pharm.data.di.IoDispatcher
import com.moon.pharm.domain.model.AuthError
import com.moon.pharm.domain.model.User
import com.moon.pharm.domain.repository.AuthRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataSource: AuthDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AuthRepository {

    override fun signUp(user: User): Flow<DataResourceResult<Unit>> = flow {
        emit(DataResourceResult.Loading)

        if (dataSource.isEmailDuplicated(user.email)) {
            emit(DataResourceResult.Failure(AuthError.EmailDuplicated()))
            return@flow
        }

        val uid = dataSource.signUp(user.email)

        dataSource.createUser(user.copy(id = uid))
        emit(DataResourceResult.Success(Unit))
    }.catch { e ->
        val error = e as? AuthError ?: AuthError.NetworkError()
        emit(DataResourceResult.Failure(e))
    }.flowOn(ioDispatcher)
}