package com.moon.pharm.domain.model.consult

import java.time.LocalDateTime

data class ConsultItem(
    val id: String,
    val userId: String,
    val expertId: String?,
    val title: String,
    val content: String,
    val status: ConsultStatus,
    val createdAt: LocalDateTime,
    val images: List<ConsultImage> = emptyList(),
    val answer: ConsultAnswer? = null
)