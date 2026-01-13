package com.moon.pharm.domain.result

sealed class DataResourceResult<out T> {
    data object Loading : DataResourceResult<Nothing>()
    data class Success<T>(val resultData: T) : DataResourceResult<T>()
    data class Failure(val exception: Throwable) : DataResourceResult<Nothing>()
}

fun <T, R> DataResourceResult<T>.mapResult(transform: (T) -> R): DataResourceResult<R> {
    return when (this) {
        is DataResourceResult.Loading -> DataResourceResult.Loading
        is DataResourceResult.Success -> DataResourceResult.Success(transform(resultData))
        is DataResourceResult.Failure -> DataResourceResult.Failure(exception)
    }
}