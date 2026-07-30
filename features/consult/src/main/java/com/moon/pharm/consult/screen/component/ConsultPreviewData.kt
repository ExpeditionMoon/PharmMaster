package com.moon.pharm.consult.screen.component

import com.moon.pharm.consult.model.ConsultStatusUiModel
import com.moon.pharm.consult.model.ConsultUiModel

object ConsultPreviewData {
    val dummyConsultItems = listOf(
        ConsultUiModel(
            id = "1", userId = "u1", pharmacistId = "p1", nickName = "사용자1",
            title = "타이레놀 복용 문의", content = "...", isPublic = true,
            status = ConsultStatusUiModel.Waiting, createdAt = System.currentTimeMillis()
        ),
        ConsultUiModel(
            id = "2", userId = "u2", pharmacistId = "p1", nickName = "비밀유저",
            title = "비밀글입니다", content = "...", isPublic = false,
            status = ConsultStatusUiModel.Completed, createdAt = System.currentTimeMillis()
        )
    )
}
