package com.moon.pharm.domain.model.consult

data class ConsultItem(
    val id: String,
    val userId: String,
    val expertId: String?,
    val title: String,
    val content: String,
    val status: ConsultStatus,
    val createdAt: Long,
    val images: List<ConsultImage> = emptyList(),
    val answer: ConsultAnswer? = null
)