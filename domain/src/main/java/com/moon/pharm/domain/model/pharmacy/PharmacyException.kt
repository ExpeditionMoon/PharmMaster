package com.moon.pharm.domain.model.pharmacy

sealed class PharmacyException(override val message: String? = null) : Exception(message) {
    class NotFound(message: String? = "약국 정보를 찾을 수 없습니다.") : PharmacyException(message)
    class NetworkError(message: String? = "네트워크 연결을 확인해주세요.") : PharmacyException(message)
    class Unknown(message: String? = "약국 정보 처리 중 오류가 발생했습니다.") : PharmacyException(message)
}