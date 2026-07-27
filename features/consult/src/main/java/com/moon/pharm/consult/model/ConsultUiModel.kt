package com.moon.pharm.consult.model

data class ConsultUiModel(
    val id: String,
    val userId: String,
    val nickName: String,
    val pharmacistId: String?,
    val title: String,
    val content: String,
    val status: ConsultStatusUiModel,
    val isPublic: Boolean,
    val createdAt: Long,
    val images: List<ConsultImageUiModel> = emptyList(),
    val answer: ConsultAnswerUiModel? = null
)

data class ConsultImageUiModel(
    val imageName: String,
    val imageUrl: String
)

data class ConsultAnswerUiModel(
    val answerId: String,
    val pharmacistId: String,
    val pharmacistName: String,
    val content: String,
    val createdAt: Long
)

enum class ConsultStatusUiModel(val label: String) {
    Waiting("답변 대기"),
    Completed("답변 완료")
}
