package com.moon.pharm.domain.result

sealed class DataResourceResult<out T> {
    data object DummyConstructor : DataResourceResult<Nothing>()
    data object Loading : DataResourceResult<Nothing>()
    data class Success<T>(val resultData: T) : DataResourceResult<T>()
    data class Failure(val exception: Throwable) : DataResourceResult<Nothing>()
}