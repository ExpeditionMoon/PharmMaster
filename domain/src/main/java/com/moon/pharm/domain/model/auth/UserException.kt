package com.moon.pharm.domain.model.auth

sealed class UserException(override val message: String? = null) : Exception(message) {
    class NotFound(message: String? = "사용자 정보를 찾을 수 없습니다.") : UserException(message)
    class NetworkError(message: String? = "네트워크 연결을 확인해주세요.") : UserException(message)
    class Unknown(message: String? = "사용자 정보 처리 중 오류가 발생했습니다.") : UserException(message)
}