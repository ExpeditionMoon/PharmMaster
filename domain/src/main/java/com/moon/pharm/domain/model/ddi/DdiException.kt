package com.moon.pharm.domain.model.ddi

sealed class DdiException(override val message: String? = null) : Exception(message) {
    class AnalysisFailed(message: String? = "약물 분석 중 오류가 발생했습니다.") : DdiException(message)
    class Network : DdiException("네트워크 연결에 실패했습니다.")
    class EmptyDrug : DdiException("추출된 약물이 없습니다.")
    class Unknown(message: String? = "알 수 없는 예외가 발생했습니다.") : DdiException(message)
}