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
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserLifeStyleRepositoryImpl @Inject constructor(
    private val dataSource: UserLifeStyleDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : UserLifeStyleRepository {

    private fun Throwable.toUserException(): UserException = when {
        this is UserException -> this
        this is FirebaseFirestoreException && this.code == FirebaseFirestoreException.Code.NOT_FOUND -> UserException.NotFound()
        this is FirebaseFirestoreException -> UserException.NetworkError()
        else -> UserException.Unknown(message)
    }

    private fun wrapCUDOperation(
        operation: suspend () -> Unit
    ): Flow<DataResourceResult<Unit>> = dataResourceFlow(
        dispatcher = ioDispatcher,
        errorMapper = { it.toUserException() },
        operation = operation
    )

    override fun getUserLifeStyle(userId: String): Flow<DataResourceResult<UserLifeStyle>> {
        return dataSource.getUserLifeStyle(userId)
            .map { dto ->
                dto?.toDomain() ?: UserLifeStyle(userId = userId)
            }
            .asDataResourceResult(
                dispatcher = ioDispatcher,
                errorMapper = { it.toUserException() }
            )
    }

    override fun saveUserLifeStyle(userLifeStyle: UserLifeStyle): Flow<DataResourceResult<Unit>> =
        wrapCUDOperation {
            dataSource.saveUserLifeStyle(userLifeStyle.userId, userLifeStyle.toDto())
        }
}
