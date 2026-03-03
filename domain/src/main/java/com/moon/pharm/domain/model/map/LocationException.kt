package com.moon.pharm.domain.model.map

sealed class LocationException(override val message: String? = null) : Exception(message) {
    class NotFound(message: String? = "현재 위치 정보를 가져올 수 없습니다.") : LocationException(message)
    class PermissionDenied(message: String? = "위치 정보 접근 권한이 필요합니다.") : LocationException(message)
    class Unknown(message: String? = "위치 정보 처리 중 알 수 없는 오류가 발생했습니다.") : LocationException(message)
}