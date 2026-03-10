package com.moon.pharm.data.repository

import com.google.firebase.firestore.FirebaseFirestoreException
import com.moon.pharm.data.datasource.UserLifeStyleDataSource
import com.moon.pharm.data.datasource.remote.dto.toDomain
import com.moon.pharm.data.datasource.remote.dto.toDto
import com.moon.pharm.data.di.IoDispatcher
import com.moon.pharm.domain.model.auth.UserException
import com.moon.pharm.domain.model.auth.UserLifeStyle
import com.moon.pharm.domain.repository.UserLifeStyleRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class UserLifeStyleRepositoryImpl @Inject constructor(
    private val dataSource: UserLifeStyleDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : UserLifeStyleRepository {

    private fun Throwable.toUserException(): UserException = when {
        this is FirebaseFirestoreException && this.code == FirebaseFirestoreException.Code.NOT_FOUND -> UserException.NotFound()
        this is FirebaseFirestoreException -> UserException.NetworkError()
        else -> UserException.Unknown(this.message)
    }

    private fun wrapCUDOperation(
        operation: suspend () -> Unit
    ): Flow<DataResourceResult<Unit>> = flow {
        emit(DataResourceResult.Loading)

        val result = runCatching {
            withTimeout(10000L) {
                operation()
            }
        }.fold(
            onSuccess = { DataResourceResult.Success(Unit) },
            onFailure = { e -> DataResourceResult.Failure(e.toUserException()) } // ⚠️ 여기는 toUserException 입니다!
        )
        emit(result)
    }.flowOn(ioDispatcher)

    override fun getUserLifeStyle(userId: String): Flow<DataResourceResult<UserLifeStyle>> {
        return dataSource.getUserLifeStyle(userId)
            .map { dto ->
                val domainModel = dto?.toDomain() ?: UserLifeStyle(userId = userId)
                DataResourceResult.Success(domainModel) as DataResourceResult<UserLifeStyle>
            }
            .onStart { emit(DataResourceResult.Loading) }
            .catch { e ->
                emit(DataResourceResult.Failure(e.toUserException()))
            }
            .flowOn(ioDispatcher)
    }

    override fun saveUserLifeStyle(userLifeStyle: UserLifeStyle): Flow<DataResourceResult<Unit>> =
        wrapCUDOperation {
            dataSource.saveUserLifeStyle(userLifeStyle.userId, userLifeStyle.toDto())
        }
}