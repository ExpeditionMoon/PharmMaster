package com.moon.pharm.domain.usecase.consult

import javax.inject.Inject

class ValidateConsultFormUseCase @Inject constructor() {
    enum class ErrorType {
        EMPTY_INPUT
    }

    sealed interface Result {
        object Valid : Result
        data class Invalid(val error: ErrorType) : Result
    }

    operator fun invoke(title: String, content: String): Result {
        if (title.isBlank() || content.isBlank()) {
            return Result.Invalid(ErrorType.EMPTY_INPUT)
        }

        return Result.Valid
    }
}