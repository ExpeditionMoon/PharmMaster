package com.moon.pharm.consult.model

// 약사 데이터 모델
data class Pharmacist(
    val id: String,
    val name: String,
    val specialty: String,
    val pharmacyName: String, // 소속 약국
    val imageResId: Int // 실제로는 URL 등
)
