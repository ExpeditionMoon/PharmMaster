package com.moon.pharm.domain.model.consult

data class ConsultAnswer(
    val answerId: String,
    val expertId: String,
    val content: String,
    val createdAt: Long
)