package com.moon.pharm.domain.model.consult

import com.moon.pharm.domain.model.auth.User

data class ConsultAnswer(
    val answerId: String,
    val pharmacistId: String,
    val pharmacistName: String,
    val userInfo: User? = null,
    val content: String,
    val createdAt: Long
)