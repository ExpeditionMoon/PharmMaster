package com.moon.pharm.domain.model.auth

sealed class PharmacistException(override val message: String? = null) : Exception(message) {
    class NotFound(message: String? = "약사 정보를 찾을 수 없습니다.") : PharmacistException(message)
    class NetworkError(message: String? = "네트워크 연결 상태를 확인해주세요.") : PharmacistException(message)
    class Unknown(message: String? = "약사 정보 처리 중 알 수 없는 오류가 발생했습니다.") : PharmacistException(message)
}