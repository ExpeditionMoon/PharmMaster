package com.moon.pharm.consult.model

import com.moon.pharm.component_ui.R

// 약사 더미 데이터
val dummyPharmacists = listOf(
    Pharmacist("1", "김미영 약사", "소아과 전문", "OO약국", R.drawable.consult_pharm),
    Pharmacist("2", "이준호 약사", "내과 전문", "OO약국", R.drawable.consult_pharm),
    Pharmacist("3", "박서연 약사", "피부과 전문", "OO약국", R.drawable.consult_pharm),
    Pharmacist("4", "최동욱 약사", "정형외과 전문", "OO약국", R.drawable.consult_pharm)
)

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

// ID로 특정 상담 아이템을 찾는 함수 (상세보기용)
fun getDummyItem(id: String?): ConsultItem {
    return if (id == "1") {
        ConsultItem(
            id = "1",
            title = "혈압약 복용 시 주의사항이 궁금합니다",
            timeAgo = "2시간 전",
            status = ConsultStatus.COMPLETED,
            isNew = true,
            author = "김OO",
            content = "요즘 고혈압으로 약을 복용 중인데, 함께 먹으면 안 되는 음식이나 생활 습관이 있을까요? 감기약이나 다른 약물과의 병용도 궁금합니다."
        )
    } else {
        ConsultItem(
            id = "3",
            title = "당뇨약과 다른 약물의 상호작용",
            timeAgo = "4시간 전",
            status = ConsultStatus.WAITING,
            isNew = false,
            author = "이OO",
            content = "제가 복용 중인 당뇨약 외에 최근 감기(혹은 두통)로 약을 먹기 시작했는데, 두 약 사이에 상호작용 문제가 없는지 궁금합니다. 주의해야 할 성분이나 복용 시간이 있나요?"
        )
    }
}