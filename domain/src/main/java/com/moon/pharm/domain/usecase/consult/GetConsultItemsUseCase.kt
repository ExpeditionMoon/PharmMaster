package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.repository.ConsultRepository

class GetConsultItemsUseCase(private val repository: ConsultRepository) {
    operator fun invoke() = repository.getConsultItems()
}