package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.repository.ConsultRepository

class CreateConsultUseCase(private val repository: ConsultRepository) {
    suspend operator fun invoke(expertId: String, title: String, content: String, images: List<String>) =
        repository.createConsult(expertId, title, content, images)
}