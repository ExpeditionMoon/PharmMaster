package com.moon.pharm.domain.model.medication

sealed class MedicationException(override val message: String? = null) : Exception(message) {
    class NotFound(message: String? = "해당 약 또는 복용 기록을 찾을 수 없습니다.") : MedicationException(message)
    class NetworkError(message: String? = "네트워크 연결 상태를 확인해주세요.") : MedicationException(message)
    class Unknown(message: String? = "기록 처리 중 알 수 없는 오류가 발생했습니다.") : MedicationException(message)
}