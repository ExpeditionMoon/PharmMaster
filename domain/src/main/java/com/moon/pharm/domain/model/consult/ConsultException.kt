package com.moon.pharm.domain.model.consult

sealed class ConsultException(override val message: String? = null) : Exception(message) {
    class NotFound : ConsultException("해당 상담 내역을 찾을 수 없습니다.")
    class Network : ConsultException("네트워크 연결에 실패했습니다.")
    class Unknown(message: String? = "알 수 없는 예외가 발생했습니다.") : ConsultException(message)
}