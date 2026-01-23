package com.moon.pharm.domain.usecase.auth

import javax.inject.Inject

class ValidateSignUpFormUseCase @Inject constructor() {

    enum class ErrorType {
        EMPTY_EMAIL,
        INVALID_EMAIL_FORMAT,
        EMPTY_PASSWORD,
        PASSWORD_TOO_SHORT,
        EMPTY_NICKNAME,
        EMPTY_PHARMACY,
        EMPTY_BIO
    }

    sealed interface Result {
        object Valid : Result
        data class Invalid(val error: ErrorType) : Result
    }

    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()

    fun validateEmailOnly(email: String): Result {
        if (email.isBlank()) return Result.Invalid(ErrorType.EMPTY_EMAIL)
        if (!email.matches(emailRegex)) return Result.Invalid(ErrorType.INVALID_EMAIL_FORMAT)
        return Result.Valid
    }

    fun validateEmailPassword(email: String, password: String): Result {
        val emailCheck = validateEmailOnly(email)
        if (emailCheck is Result.Invalid) return emailCheck

        if (password.isBlank()) return Result.Invalid(ErrorType.EMPTY_PASSWORD)
        if (password.length < 6) return Result.Invalid(ErrorType.PASSWORD_TOO_SHORT)

        return Result.Valid
    }

    fun validateNickname(nickname: String): Result {
        if (nickname.isBlank()) return Result.Invalid(ErrorType.EMPTY_NICKNAME)
        return Result.Valid
    }

    fun validatePharmacistInfo(pharmacySelected: Boolean, bio: String): Result {
        if (!pharmacySelected) return Result.Invalid(ErrorType.EMPTY_PHARMACY)
        if (bio.isBlank()) return Result.Invalid(ErrorType.EMPTY_BIO)
        return Result.Valid
    }
}