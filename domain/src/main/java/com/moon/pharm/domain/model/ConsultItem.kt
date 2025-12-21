package com.moon.pharm.domain.model

data class ConsultItem(
    val id: String,
    val userId: String,
    val expertId: String?,
    val title: String,
    val content: String,
    val status: ConsultStatus,
    val createdAt: String,
    val images: List<ConsultImage> = emptyList(),
    val answer: ConsultAnswer? = null
)

data class ConsultImage(
    val imageName: String
)

data class ConsultAnswer(
    val answerId: String,
    val expertId: String,
    val content: String,
    val createdAt: String
)

enum class ConsultStatus(val label: String) {
    WAITING("답변 대기"),
    COMPLETED("답변 완료")
}
