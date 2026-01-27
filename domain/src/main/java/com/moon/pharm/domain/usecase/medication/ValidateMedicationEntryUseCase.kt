package com.moon.pharm.domain.usecase.medication

import javax.inject.Inject

class ValidateMedicationEntryUseCase @Inject constructor() {

    sealed interface Result {
        object Success : Result
        data class Error(val error: MedicationValidatorError) : Result
    }

    enum class MedicationValidatorError {
        EMPTY_NAME,
    }

    operator fun invoke(name: String): Result {
        if (name.isBlank()) {
            return Result.Error(MedicationValidatorError.EMPTY_NAME)
        }
        return Result.Success
    }
}