package com.moon.pharm.data.repository

import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

private const val DEFAULT_OPERATION_TIMEOUT_MILLIS = 10_000L

internal suspend fun <T> runDataResourceOperation(
    dispatcher: CoroutineDispatcher,
    timeoutMillis: Long = DEFAULT_OPERATION_TIMEOUT_MILLIS,
    errorMapper: (Throwable) -> Throwable,
    operation: suspend () -> T
): DataResourceResult<T> = withContext(dispatcher) {
    runCatching {
        withTimeout(timeoutMillis) {
            operation()
        }
    }.fold(
        onSuccess = { DataResourceResult.Success(it) },
        onFailure = { DataResourceResult.Failure(errorMapper(it)) }
    )
}

internal fun <T> dataResourceFlow(
    dispatcher: CoroutineDispatcher,
    timeoutMillis: Long = DEFAULT_OPERATION_TIMEOUT_MILLIS,
    errorMapper: (Throwable) -> Throwable,
    operation: suspend () -> T
): Flow<DataResourceResult<T>> = flow {
    emit(DataResourceResult.Loading)

    val result = runCatching {
        withTimeout(timeoutMillis) {
            operation()
        }
    }.fold(
        onSuccess = { DataResourceResult.Success(it) },
        onFailure = { DataResourceResult.Failure(errorMapper(it)) }
    )

    emit(result)
}.flowOn(dispatcher)

internal fun <T> Flow<T>.asDataResourceResult(
    dispatcher: CoroutineDispatcher,
    errorMapper: (Throwable) -> Throwable
): Flow<DataResourceResult<T>> = map<T, DataResourceResult<T>> { DataResourceResult.Success(it) }
    .onStart { emit(DataResourceResult.Loading) }
    .catch { emit(DataResourceResult.Failure(errorMapper(it))) }
    .flowOn(dispatcher)
