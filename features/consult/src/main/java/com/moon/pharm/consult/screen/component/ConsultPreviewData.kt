package com.moon.pharm.consult.screen.component

import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.model.consult.ConsultStatus

object ConsultPreviewData {
    val dummyConsultItems = listOf(
        ConsultItem(
            id = "1", userId = "u1", pharmacistId = "p1", nickName = "사용자1",
            title = "타이레놀 복용 문의", content = "...", isPublic = true,
            status = ConsultStatus.WAITING, createdAt = System.currentTimeMillis()
        ),
        ConsultItem(
            id = "2", userId = "u2", pharmacistId = "p1", nickName = "비밀유저",
            title = "비밀글입니다", content = "...", isPublic = false,
            status = ConsultStatus.COMPLETED, createdAt = System.currentTimeMillis()
        )
    )
}