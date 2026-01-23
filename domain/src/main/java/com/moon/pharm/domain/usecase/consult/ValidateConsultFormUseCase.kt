package com.moon.pharm.domain.usecase.consult

import javax.inject.Inject

class ValidateConsultFormUseCase @Inject constructor() {
    enum class ErrorType {
        EMPTY_INPUT, TITLE_TOO_SHORT
    }

    sealed interface Result {
        object Valid : Result
        data class Invalid(val error: ErrorType) : Result
    }

    operator fun invoke(title: String, content: String): Result {
        if (title.isBlank() || content.isBlank()) {
            return Result.Invalid(ErrorType.EMPTY_INPUT)
        }
        if (title.length < 5) {
            return Result.Invalid(ErrorType.TITLE_TOO_SHORT)
        }

        return Result.Valid
    }
}