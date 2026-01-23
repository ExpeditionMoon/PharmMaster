package com.moon.pharm.domain.usecase.auth

import javax.inject.Inject

class ValidateLoginFormUseCase @Inject constructor() {

    enum class ErrorType {
        EMPTY_EMAIL,
        EMPTY_PASSWORD
    }

    sealed interface Result {
        object Valid : Result
        data class Invalid(val error: ErrorType) : Result
    }

    operator fun invoke(email: String, password: String): Result {
        if (email.isBlank()) {
            return Result.Invalid(ErrorType.EMPTY_EMAIL)
        }
        if (password.isBlank()) {
            return Result.Invalid(ErrorType.EMPTY_PASSWORD)
        }
        return Result.Valid
    }
}