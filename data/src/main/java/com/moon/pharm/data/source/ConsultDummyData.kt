package com.moon.pharm.data.source

import com.moon.pharm.component_ui.R
import com.moon.pharm.domain.model.ConsultAnswer
import com.moon.pharm.domain.model.ConsultImage
import com.moon.pharm.domain.model.ConsultItem
import com.moon.pharm.domain.model.ConsultStatus
import com.moon.pharm.domain.model.Pharmacist

object ConsultDummyData {
    // 약사 더미 데이터
    val dummyPharmacists = listOf(
        Pharmacist("1", "김미영 약사", "소아과 전문", "P001", "OO약국", true, R.drawable.consult_pharm.toString()),
        Pharmacist("2", "이준호 약사", "내과 전문", "P002", "OO약국", true, R.drawable.consult_pharm.toString()),
        Pharmacist("3", "박서연 약사", "피부과 전문", "P003", "OO약국", true, R.drawable.consult_pharm.toString()),
        Pharmacist("4", "최동욱 약사", "정형외과 전문", "P004", "OO약국", true, R.drawable.consult_pharm.toString())
    )

    // 상담 리스트 더미 데이터
    val dummyConsultItems = listOf(
        ConsultItem(
            id = "1", userId = "user_13", expertId = null,
            title = "당뇨약과 다른 약물의 상호작용",
            content = "한약과 당뇨약을 병용해도 안전할까요?",
            status = ConsultStatus.WAITING,
            createdAt = "2025-03-10"
        ),
        ConsultItem(
            id = "2", userId = "user_12", expertId = "expert_1",
            title = "감기약 복용 후 졸음 증상",
            content = "졸리지 않은 감기약은 효과가 떨어지나요?",
            status = ConsultStatus.COMPLETED,
            createdAt = "2025-03-11",
            answer = ConsultAnswer("ans_2", "expert_1", "효과가 떨어지는 것이 아니라 부작용만 뺀 것입니다.", "2025-03-12")
        ),
        ConsultItem(
            id = "3", userId = "user_11", expertId = null,
            title = "비타민 영양제 복용 시간",
            content = "멀티 비타민은 식전과 식후 중 언제 먹는 게 좋나요?",
            status = ConsultStatus.WAITING,
            createdAt = "2025-03-12"
        ),
        ConsultItem(
            id = "4", userId = "user_10", expertId = "expert_2",
            title = "위장약 복용 문의",
            content = "속쓰림 때문에 겔포스를 자주 먹는데 매일 먹어도 되나요?",
            status = ConsultStatus.COMPLETED,
            createdAt = "2025-03-13",
            answer = ConsultAnswer("ans_4", "expert_2", "장기 복용 시 원인 파악이 중요하므로 내원 권장합니다.", "2025-03-14")
        ),
        ConsultItem(
            id = "5", userId = "user_9", expertId = null,
            title = "오메가3 고르는 법",
            content = "rTG 오메가3가 일반 제품보다 흡수율이 훨씬 높은가요?",
            status = ConsultStatus.WAITING,
            createdAt = "2025-03-14"
        ),
        ConsultItem(
            id = "6", userId = "user_8", expertId = "expert_3",
            title = "유산균 보관 방법",
            content = "냉장 보관용 유산균을 실온에 하루 두었는데 괜찮을까요?",
            status = ConsultStatus.COMPLETED,
            createdAt = "2025-03-15",
            answer = ConsultAnswer("ans_6", "expert_3", "균주 사멸 위험이 있으나 하루 정도는 큰 문제 없습니다.", "2025-03-15")
        ),
        ConsultItem(
            id = "7", userId = "user_7", expertId = null,
            title = "피부과 약과 커피",
            content = "여드름 약 복용 중인데 커피 마시면 부작용이 있나요?",
            status = ConsultStatus.WAITING,
            createdAt = "2025-03-16"
        ),
        ConsultItem(
            id = "8", userId = "user_6", expertId = "expert_1",
            title = "두통약 내성 걱정",
            content = "타이레놀을 너무 자주 먹으면 나중에 효과가 없나요?",
            status = ConsultStatus.COMPLETED,
            createdAt = "2025-03-17",
            answer = ConsultAnswer("ans_8", "expert_1", "아세트아미노펜 성분은 비교적 내성 걱정이 적은 편입니다.", "2025-03-17")
        ),
        ConsultItem(
            id = "9", userId = "user_5", expertId = null,
            title = "안약 유통기한 문의",
            content = "개봉한 지 3개월 된 안약, 눈에 넣어도 될까요?",
            status = ConsultStatus.WAITING,
            createdAt = "2025-03-18"
        ),
        ConsultItem(
            id = "10", userId = "user_4", expertId = null,
            title = "항생제 복용 중 음주 가능한지 문의",
            content = "항생제 처방 후 맥주 한 잔은 괜찮나요?",
            status = ConsultStatus.WAITING,
            createdAt = "2025-03-19"
        ),
        ConsultItem(
            id = "11", userId = "user_3", expertId = null,
            title = "당뇨약과 다른 약물의 상호작용",
            content = "당뇨약 복용 중 타이레놀 먹어도 되나요?",
            status = ConsultStatus.WAITING,
            createdAt = "2025-03-20",
            images = listOf(ConsultImage("medicine.jpg"))
        ),
        ConsultItem(
            id = "12", userId = "user_2", expertId = "expert_2",
            title = "감기약 복용 후 졸음 증상",
            content = "감기약 먹고 운전해야 하는데 너무 졸리네요.",
            status = ConsultStatus.COMPLETED,
            createdAt = "2025-03-21",
            answer = ConsultAnswer("ans_12", "expert_2", "졸음이 적은 2세대 약을 권장합니다.", "2025-03-21")
        ),
        ConsultItem(
            id = "13", userId = "user_1", expertId = "expert_1",
            title = "혈압약 복용 시 주의사항이 궁금합니다",
            content = "요즘 고혈압 약 복용 중인데 피해야 할 음식이 있나요?",
            status = ConsultStatus.COMPLETED,
            createdAt = "2025-03-22",
            answer = ConsultAnswer("ans_13", "expert_1", "자몽주스는 약의 농도를 높이니 피하세요.", "2025-03-22")
        )
    )
}