package com.moon.pharm.domain.model.consult

import java.time.LocalDateTime

data class ConsultAnswer(
    val answerId: String,
    val expertId: String,
    val content: String,
    val createdAt: LocalDateTime
)