package com.moon.pharm.domain.model.drug

data class Drug(
    val itemSeq: String,     // 품목기준코드 (고유 식별자)
    val itemName: String,    // 제품명 (예: 타이레놀정500밀리그람)
    val companyName: String, // 제조사 (예: 한국존슨앤드존슨판매(유))
    val efficacy: String,    // 효능 (AI 요약 및 상세화면 표출용)
    val interaction: String, // 상호작용 주의사항 (AI DDI 분석 핵심 데이터)
    val imageUrl: String     // 약품 이미지 URL
)