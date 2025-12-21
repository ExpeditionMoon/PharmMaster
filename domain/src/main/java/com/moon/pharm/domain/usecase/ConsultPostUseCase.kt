package com.moon.pharm.domain.usecase

import com.moon.pharm.domain.repository.ConsultRepository

class ConsultPostUseCase(private val repository: ConsultRepository) {
    suspend operator fun invoke(expertId: String, title: String, content: String, images: List<String>) =
        repository.consultPost(expertId, title, content, images)
}