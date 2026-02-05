package com.moon.pharm.domain.model.consult

data class ConsultItem(
    val id: String,
    val userId: String,
    val nickName: String,
    val pharmacistId: String? = null,
    val title: String,
    val content: String,
    val status: ConsultStatus,
    val isPublic: Boolean,
    val createdAt: Long,
    val images: List<ConsultImage> = emptyList(),
    val answer: ConsultAnswer? = null
)