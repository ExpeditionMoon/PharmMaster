package com.moon.pharm.domain.model.auth

sealed class AuthError : Exception() {
    class EmailDuplicated : AuthError()
    class SignUpFailed : AuthError()
    class NetworkError : AuthError()
}