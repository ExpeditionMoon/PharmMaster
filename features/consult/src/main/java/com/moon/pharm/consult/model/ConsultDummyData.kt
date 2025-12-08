package com.moon.pharm.consult.model

// 상담 리스트 더미 데이터
val dummyConsultItems = listOf(
    ConsultItem("1", "혈압약 복용 시 주의사항이 궁금합니다", "2시간 전", ConsultStatus.COMPLETED, true),
    ConsultItem("2", "감기약 복용 후 졸음 증상", "6시간 전", ConsultStatus.COMPLETED, false),
    ConsultItem("3", "당뇨약과 다른 약물의 상호작용", "4시간 전", ConsultStatus.WAITING, true),
    ConsultItem("4", "항생제 복용 중 음주 가능한지 문의", "8시간 전", ConsultStatus.WAITING, false),
    ConsultItem("5", "소화제 복용법과 효과적인 복용 시간", "12시간 전", ConsultStatus.COMPLETED, true),
    ConsultItem("6", "알레르기 약물과 음식 섭취 관련", "1일 전", ConsultStatus.WAITING, false),
    ConsultItem("7", "비타민 보충제 복용 순서 문의", "1일 전", ConsultStatus.WAITING, true),
    ConsultItem("8", "진통제 장기 복용의 부작용", "2일 전", ConsultStatus.COMPLETED, false),
    ConsultItem("9", "임신 중 복용 가능한 약물 문의", "3일 전", ConsultStatus.WAITING, false),
    ConsultItem("10", "수면제 의존성에 대한 질문", "4일 전", ConsultStatus.COMPLETED, true),
    ConsultItem("11", "혈압약 복용 시 주의사항이 궁금합니다", "5일 전", ConsultStatus.COMPLETED, true),
    ConsultItem("12", "감기약 복용 후 졸음 증상", "6일 전", ConsultStatus.COMPLETED, false),
    ConsultItem("13", "당뇨약과 다른 약물의 상호작용", "6일 전", ConsultStatus.WAITING, true),
)
