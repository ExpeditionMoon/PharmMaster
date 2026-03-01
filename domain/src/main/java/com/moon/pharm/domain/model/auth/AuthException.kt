package com.moon.pharm.domain.model.auth

sealed class AuthException(override val message: String? = null) : Exception(message) {
    class EmailDuplicated(message: String? = "이미 가입된 이메일입니다.") : AuthException(message)
    class SignUpFailed(message: String? = "회원가입에 실패했습니다.") : AuthException(message)
    class InvalidCredentials(message: String? = "이메일 또는 비밀번호가 일치하지 않습니다.") : AuthException(message)
    class NetworkError(message: String? = "네트워크 연결을 확인해주세요.") : AuthException(message)
    class Unknown(message: String? = "인증 중 알 수 없는 오류가 발생했습니다.") : AuthException(message)
}